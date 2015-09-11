package org.metadatacenter.jsonss.parser.node;

import org.metadatacenter.jsonss.parser.ASTReference;
import org.metadatacenter.jsonss.parser.ASTValueExtractionFunction;
import org.metadatacenter.jsonss.parser.InternalParseException;
import org.metadatacenter.jsonss.parser.Node;
import org.metadatacenter.jsonss.parser.ParseException;
import org.metadatacenter.jsonss.parser.ParserUtil;
import org.metadatacenter.jsonss.parser.ASTValueSpecificationItem;

public class ValueSpecificationItemNode implements JSONSSNode
{
  private final String stringLiteral;
  private final String capturingExpression;
  private ReferenceNode referenceNode;
  private ValueExtractionFunctionNode valueExtractionFunctionNode;

  public ValueSpecificationItemNode(ASTValueSpecificationItem node) throws ParseException
  {
    this.stringLiteral = node.stringLiteral;
    this.capturingExpression = node.captureExpression;

    if (node.jjtGetNumChildren() != 0) {
      Node child = node.jjtGetChild(0);

      if (ParserUtil.hasName(child, "Reference"))
        this.referenceNode = new ReferenceNode((ASTReference)child);
      else if (ParserUtil.hasName(child, "ValueExtractionFunction"))
        this.valueExtractionFunctionNode = new ValueExtractionFunctionNode((ASTValueExtractionFunction)child);
      else
        throw new InternalParseException("invalid child node " + child + " for node " + getNodeName());
    }
  }

	@Override public String getNodeName()
	{
		return "ValueSpecificationItem";
	}

	public boolean hasStringLiteral()
  {
    return this.stringLiteral != null;
  }

  public String getStringLiteral()
  {
    return this.stringLiteral;
  }

  public boolean hasReferenceNode()
  {
    return this.referenceNode != null;
  }

  public ReferenceNode getReferenceNode()
  {
    return this.referenceNode;
  }

  public boolean hasValueExtractionFunctionNode()
  {
    return this.valueExtractionFunctionNode != null;
  }

  public ValueExtractionFunctionNode getValueExtractionFunctionNode()
  {
    return this.valueExtractionFunctionNode;
  }

  public boolean hasCapturingExpression()
  {
    return this.capturingExpression != null;
  }

  public String getCapturingExpression()
  {
    return this.capturingExpression;
  }

  public String toString()
  {
    String representation = "";

    if (hasStringLiteral())
      representation = "\"" + this.stringLiteral + "\"";
    else if (hasReferenceNode())
      representation = this.referenceNode.toString();
    else if (hasValueExtractionFunctionNode())
      representation = this.valueExtractionFunctionNode.toString();
    else if (hasCapturingExpression())
      representation = "[\"" + this.capturingExpression + "\"]";

    return representation;
  }

  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null || obj.getClass() != this.getClass())
      return false;
    ValueSpecificationItemNode vs = (ValueSpecificationItemNode)obj;
    return this.stringLiteral != null && vs.stringLiteral != null && this.stringLiteral.equals(vs.stringLiteral) &&
      this.referenceNode != null && vs.referenceNode != null && this.referenceNode.equals(vs.referenceNode) &&
      this.valueExtractionFunctionNode != null && vs.valueExtractionFunctionNode != null
      && this.valueExtractionFunctionNode.equals(vs.valueExtractionFunctionNode) && this.capturingExpression != null
      && vs.capturingExpression != null && this.capturingExpression.equals(vs.capturingExpression);
  }

  public int hashCode()
  {
    int hash = 15;

    hash = hash + (null == this.stringLiteral ? 0 : this.stringLiteral.hashCode());
    hash = hash + (null == this.referenceNode ? 0 : this.referenceNode.hashCode());
    hash = hash + (null == this.valueExtractionFunctionNode ? 0 : this.valueExtractionFunctionNode.hashCode());
    hash = hash + (null == this.capturingExpression ? 0 : this.capturingExpression.hashCode());

    return hash;
  }
}
