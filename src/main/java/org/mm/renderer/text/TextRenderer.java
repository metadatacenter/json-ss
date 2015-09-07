package org.mm.renderer.text;

import org.mm.core.ReferenceType;
import org.mm.parser.JSONSSParserConstants;
import org.mm.parser.node.MMExpressionNode;
import org.mm.parser.node.ReferenceNode;
import org.mm.parser.node.SourceSpecificationNode;
import org.mm.parser.node.StringLiteralNode;
import org.mm.parser.node.ValueExtractionFunctionArgumentNode;
import org.mm.parser.node.ValueExtractionFunctionNode;
import org.mm.renderer.InternalRendererException;
import org.mm.renderer.ReferenceRenderer;
import org.mm.renderer.ReferenceRendererConfiguration;
import org.mm.renderer.ReferenceUtil;
import org.mm.renderer.Renderer;
import org.mm.renderer.RendererException;
import org.mm.rendering.ReferenceRendering;
import org.mm.rendering.StringLiteralRendering;
import org.mm.rendering.text.TextReferenceRendering;
import org.mm.rendering.text.TextRendering;
import org.mm.rendering.text.TextStringLiteralRendering;
import org.mm.ss.SpreadSheetDataSource;
import org.mm.ss.SpreadsheetLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TextRenderer extends ReferenceRendererConfiguration
    implements Renderer, ReferenceRenderer, JSONSSParserConstants
{
  private SpreadSheetDataSource dataSource;

  public TextRenderer(SpreadSheetDataSource dataSource)
  {
    this.dataSource = dataSource;
  }

  @Override public void changeDataSource(SpreadSheetDataSource dataSource)
  {
    // Logging data source has been updated
    this.dataSource = dataSource;
  }

  @Override public ReferenceRendererConfiguration getReferenceRendererConfiguration()
  {
    return this;
  }

  @Override public Optional<? extends TextRendering> renderExpression(MMExpressionNode expressionNode)
      throws RendererException
  {
    throw new InternalRendererException("not implemented " + expressionNode.getNodeName());
  }

  // TODO Refactor - too long
  @Override public Optional<TextReferenceRendering> renderReference(ReferenceNode referenceNode)
      throws RendererException
  {
    SourceSpecificationNode sourceSpecificationNode = referenceNode.getSourceSpecificationNode();
    ReferenceType referenceType = referenceNode.getReferenceTypeNode().getReferenceType();

    if (sourceSpecificationNode.hasLiteral()) {
      String literalValue = sourceSpecificationNode.getLiteral();
      return Optional.of(new TextReferenceRendering(literalValue, referenceType));
    } else {
      SpreadsheetLocation location = ReferenceUtil.resolveLocation(dataSource, referenceNode);
      String resolvedReferenceValue = ReferenceUtil.resolveReferenceValue(dataSource, referenceNode);

      if (referenceType.isUntyped())
        throw new RendererException("untyped reference " + referenceNode);

      if (resolvedReferenceValue.isEmpty()
          && referenceNode.getActualEmptyLocationDirective() == MM_SKIP_IF_EMPTY_LOCATION)
        return Optional.empty();

      if (resolvedReferenceValue.isEmpty()
          && referenceNode.getActualEmptyLocationDirective() == MM_WARNING_IF_EMPTY_LOCATION) {
        // TODO Warn in log files
        return Optional.empty();
      }

      if (referenceType.isLiteral()) { // Reference is an OWL literal
        String literalReferenceValue = processOWLLiteralReferenceValue(location, resolvedReferenceValue, referenceNode);

        if (literalReferenceValue.isEmpty()
            && referenceNode.getActualEmptyLiteralDirective() == MM_SKIP_IF_EMPTY_LITERAL)
          return Optional.empty();

        if (literalReferenceValue.isEmpty()
            && referenceNode.getActualEmptyLiteralDirective() == MM_WARNING_IF_EMPTY_LITERAL) {
          // TODO Warn in log file
          return Optional.empty();
        }

        return Optional.of(new TextReferenceRendering(literalReferenceValue, referenceType));
      } else
        throw new InternalRendererException(
            "unknown reference type " + referenceType + " for reference " + referenceNode);
    }
  }

  private String processOWLLiteralReferenceValue(SpreadsheetLocation location, String rawLocationValue,
      ReferenceNode referenceNode) throws RendererException
  {
    String sourceValue = rawLocationValue.replace("\"", "\\\"");
    String processedReferenceValue = "";

    if (sourceValue.isEmpty() && !referenceNode.getActualDefaultLiteral().isEmpty())
      processedReferenceValue = referenceNode.getActualDefaultLiteral();

    if (processedReferenceValue.isEmpty()
        && referenceNode.getActualEmptyLiteralDirective() == MM_ERROR_IF_EMPTY_LITERAL)
      throw new RendererException("empty literal in reference " + referenceNode + " at location " + location);

    return processedReferenceValue;
  }

  // Tentative. Need a more principled way of finding and invoking functions. What about calls to Excel?

  private String generateReferenceValue(String sourceValue, ValueExtractionFunctionNode valueExtractionFunctionNode)
      throws RendererException
  {
    List<String> arguments = new ArrayList<>();
    if (valueExtractionFunctionNode.hasArguments()) {
      for (ValueExtractionFunctionArgumentNode argumentNode : valueExtractionFunctionNode.getArgumentNodes()) {
        String argumentValue = generateValueExtractionFunctionArgument(argumentNode);
        arguments.add(argumentValue);
      }
    }
    return ReferenceUtil.evaluateReferenceValue(valueExtractionFunctionNode.getFunctionName(),
        valueExtractionFunctionNode.getFunctionID(), arguments, sourceValue,
        valueExtractionFunctionNode.hasArguments());
  }

  private Optional<? extends StringLiteralRendering> renderStringLiteral(StringLiteralNode stringLiteralNode)
  {
    return Optional.of(new TextStringLiteralRendering(stringLiteralNode.getValue()));
  }

  /**
   * Arguments to value extraction functions cannot be dropped if the reference resolves to nothing.
   */
  private String generateValueExtractionFunctionArgument(
      ValueExtractionFunctionArgumentNode valueExtractionFunctionArgumentNode) throws RendererException
  {
    if (valueExtractionFunctionArgumentNode.isStringLiteralNode()) {
      Optional<? extends StringLiteralRendering> literalRendering = renderStringLiteral(
          valueExtractionFunctionArgumentNode.getStringLiteralNode());
      if (literalRendering.isPresent()) {
        return literalRendering.get().getRawValue();
      } else
        throw new RendererException("empty literal for value extraction function argument");
    } else if (valueExtractionFunctionArgumentNode.isReferenceNode()) {
      ReferenceNode referenceNode = valueExtractionFunctionArgumentNode.getReferenceNode();
      Optional<? extends ReferenceRendering> referenceRendering = renderReference(referenceNode);
      if (referenceRendering.isPresent()) {
        if (referenceRendering.get().isLiteral()) {
          return referenceRendering.get().getRawValue();
        } else
          throw new RendererException("expecting literal reference for value extraction function argument, got "
              + valueExtractionFunctionArgumentNode);
      } else
        throw new RendererException("empty reference " + referenceNode + " for value extraction function argument");
    } else
      throw new InternalRendererException(
          "unknown child for node " + valueExtractionFunctionArgumentNode.getNodeName());
  }
}