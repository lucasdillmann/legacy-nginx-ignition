import React from "react"
import { Button, Flex, Form, FormListFieldData, FormListOperation, Input } from "antd"
import FormLayout from "../../../core/components/form/FormLayout"
import If from "../../../core/components/flowcontrol/If"
import { CloseOutlined, PlusOutlined } from "@ant-design/icons"
import ValidationResult from "../../../core/validation/ValidationResult"

export interface DomainNamesListProps {
    validationResult: ValidationResult
    expandedLabelSize?: boolean
    className?: string
}

export default class DomainNamesList extends React.PureComponent<DomainNamesListProps> {
    private renderFields(fields: FormListFieldData[], operations: FormListOperation) {
        const { validationResult, expandedLabelSize, className } = this.props
        const layout = expandedLabelSize === true ? FormLayout.ExpandedUnlabeledItem : FormLayout.UnlabeledItem

        const domainNameFields = fields.map((field, index) => (
            <Form.Item
                {...(index > 0 ? layout : undefined)}
                label={index === 0 ? "Domain names" : ""}
                key={field.key}
                className={className}
                required
            >
                <Flex>
                    <Form.Item
                        {...field}
                        validateStatus={validationResult.getStatus(`domainNames[${index}]`)}
                        help={validationResult.getMessage(`domainNames[${index}]`)}
                        style={{ marginBottom: 0, width: "100%" }}
                    >
                        <Input placeholder="Domain name" />
                    </Form.Item>
                    <If condition={fields.length > 1}>
                        <CloseOutlined
                            style={{ marginLeft: 15, alignItems: "start", marginTop: 7 }}
                            onClick={() => operations.remove(field.name)}
                        />
                    </If>
                </Flex>
            </Form.Item>
        ))

        const addAction = (
            <Form.Item {...layout} className={className}>
                <Button type="dashed" onClick={() => operations.add()} icon={<PlusOutlined />}>
                    Add domain
                </Button>
            </Form.Item>
        )

        return [...domainNameFields, addAction]
    }

    render() {
        return <Form.List name="domainNames">{(fields, operations) => this.renderFields(fields, operations)}</Form.List>
    }
}
