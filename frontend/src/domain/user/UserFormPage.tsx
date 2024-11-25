import React from "react";
import ShellAwareComponent, {ShellConfig} from "../../core/components/shell/ShellAwareComponent";
import {navigateTo, routeParams} from "../../core/components/router/AppRouter";
import UserRequest from "./model/UserRequest";
import {UserRole} from "./model/UserRole";
import UserService from "./UserService";
import {Empty, Form, Input, Select, Switch} from "antd";
import Preloader from "../../core/components/preloader/Preloader";
import FormLayout from "../../core/components/form/FormLayout";
import ValidationResult from "../../core/validation/ValidationResult";
import Password from "antd/es/input/Password";
import ModalPreloader from "../../core/components/preloader/ModalPreloader";
import Notification from "../../core/components/notification/Notification";
import {UnexpectedResponseError} from "../../core/apiclient/ApiResponse";
import ValidationResultConverter from "../../core/validation/ValidationResultConverter";
import UserResponse from "./model/UserResponse";

interface UserFormState {
    formValues: UserRequest
    validationResult: ValidationResult
    loading: boolean
    notFound: boolean
}

export default class UserFormPage extends ShellAwareComponent<unknown, UserFormState> {
    private readonly userId?: string
    private readonly service: UserService
    private readonly saveModal: ModalPreloader

    constructor(props: any, context: any) {
        super(props, context);
        const userId = routeParams().id
        this.userId = userId === "new" ? undefined : userId
        this.service = new UserService()
        this.saveModal = new ModalPreloader()
        this.state = {
            formValues: {
                name: "",
                enabled: true,
                role: UserRole.REGULAR_USER,
                username: "",
            },
            validationResult: new ValidationResult(),
            loading: true,
            notFound: false,
        }
    }

    private submit() {
        const {formValues} = this.state
        this.saveModal.show("Hang on tight", "We're saving the user")

        const action = this.userId === undefined
            ? this.service.create(formValues)
            : this.service.updateById(this.userId, formValues)

        action
            .then(() => this.handleSuccess())
            .catch(error => this.handleError(error))
            .then(() => this.saveModal.close())
    }

    private handleSuccess() {
        Notification.success("User saved", "The user was saved successfully")
        navigateTo("/users")
    }

    private handleError(error: Error) {
        if (error instanceof UnexpectedResponseError) {
            const validationResult = ValidationResultConverter.parse(error.response)
            if (validationResult != null)
                this.setState({ validationResult })
        }

        Notification.error(
            "That didn't work",
            "Please check the form to see if everything seems correct",
        )
    }

    private passwordHelpText() {
        return this.userId === undefined
            ? undefined
            : "Leave empty if you want to keep the user's password unchanged"
    }

    private renderForm() {
        const {validationResult, formValues} = this.state

        return (
            <Form<UserRequest>
                {...FormLayout.FormDefaults}
                onValuesChange={(_, formValues) => this.setState({formValues})}
                initialValues={formValues}
            >
                <Form.Item
                    name="enabled"
                    validateStatus={validationResult.getStatus("enabled")}
                    help={validationResult.getMessage("enabled")}
                    label="Enabled"
                    required
                >
                    <Switch />
                </Form.Item>
                <Form.Item
                    name="name"
                    validateStatus={validationResult.getStatus("name")}
                    help={validationResult.getMessage("name")}
                    label="Name"
                    required
                >
                    <Input />
                </Form.Item>
                <Form.Item
                    name="username"
                    validateStatus={validationResult.getStatus("username")}
                    help={validationResult.getMessage("username")}
                    label="Username"
                    required
                >
                    <Input />
                </Form.Item>
                <Form.Item
                    name="password"
                    validateStatus={validationResult.getStatus("password")}
                    help={validationResult.getMessage("password") ?? this.passwordHelpText()}
                    label="Password"
                    required={this.userId === undefined}
                >
                    <Password />
                </Form.Item>
                <Form.Item
                    name="role"
                    validateStatus={validationResult.getStatus("role")}
                    help={validationResult.getMessage("role")}
                    label="Role"
                    required
                >
                    <Select>
                        <Select.Option value={UserRole.ADMIN}>Admin</Select.Option>
                        <Select.Option value={UserRole.REGULAR_USER}>Regular user</Select.Option>
                    </Select>
                </Form.Item>
            </Form>
        )
    }

    private convertToUserRequest(response: UserResponse): UserRequest {
        const {enabled, name, username, role} = response
        return {enabled, name, username, role}
    }

    componentDidMount() {
        if (this.userId === undefined) {
            this.setState({loading: false})
            return
        }

        this.service
            .getById(this.userId!!)
            .then(userDetails => {
                if (userDetails === undefined)
                    this.setState({loading: false, notFound: true})
                else
                    this.setState({loading: false, formValues: this.convertToUserRequest(userDetails)})
            })
    }

    shellConfig(): ShellConfig {
        return {
            title: "User details",
            subtitle: "Full details and configurations of the nginx ignition's user",
            actions: [
                {
                    description: "Save",
                    onClick: () => this.submit(),
                },
            ],
        };
    }

    render() {
        const {loading, notFound} = this.state

        if (notFound)
            return <Empty description="Not found" />

        if (loading)
            return <Preloader loading />

        return this.renderForm()
    }
}
