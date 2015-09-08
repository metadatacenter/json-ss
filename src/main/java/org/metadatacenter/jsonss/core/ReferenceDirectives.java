package org.metadatacenter.jsonss.core;

import org.metadatacenter.jsonss.parser.DefaultReferenceDirectives;
import org.metadatacenter.jsonss.parser.JSONSSParserConstants;
import org.metadatacenter.jsonss.ss.SpreadsheetLocation;

public class ReferenceDirectives implements JSONSSParserConstants
{
  private final DefaultReferenceDirectives defaultReferenceDirectives;
  private boolean hasExplicitlySpecifiedReferenceType;
  private boolean hasExplicitlySpecifiedDefaultLocationValue;
  private boolean hasExplicitlySpecifiedDefaultLiteral;
  private boolean hasExplicitlySpecifiedShiftDirective;
  private boolean hasExplicitlySpecifiedEmptyLocationDirective;
  private boolean hasExplicitlySpecifiedEmptyLiteralDirective;
  private ReferenceType explicitlySpecifiedReferenceType;
  private String explicitlySpecifiedDefaultLocationValue;
  private String explicitlySpecifiedDefaultLiteral;
  private int explicitlySpecifiedShiftDirective = -1;
  private int explicitlySpecifiedEmptyLocationDirective = -1;
  private int explicitlySpecifiedEmptyLiteralDirective = -1;

  private boolean usesLocationEncoding;
  private boolean usesLocationWithDuplicatesEncoding;
  private SpreadsheetLocation shiftedLocation;

  private boolean hasExplicitlySpecifiedOptions;

  public ReferenceDirectives(DefaultReferenceDirectives defaultReferenceDirectives)
  {
    this.defaultReferenceDirectives = defaultReferenceDirectives;
  }

  public boolean hasExplicitlySpecifiedOptions()
  {
    return this.hasExplicitlySpecifiedOptions;
  }

  public void setUsesLocationEncoding()
  {
    this.usesLocationEncoding = true;
  }

  public void setUsesLocationWithDuplicatesEncoding()
  {
    this.usesLocationWithDuplicatesEncoding = true;
  }

  public boolean usesLocationEncoding()
  {
    return this.usesLocationEncoding;
  }

  public boolean usesLocationWithDuplicatesEncoding()
  {
    return this.usesLocationWithDuplicatesEncoding;
  }

  public int getDefaultShiftDirective()
  {
    return this.defaultReferenceDirectives.getDefaultShiftDirective();
  }

  public void setDefaultShiftDirective(int shiftDirective)
  {
    this.defaultReferenceDirectives.setDefaultShiftDirective(shiftDirective);
  }

  public String getDefaultLocationValue()
  {
    return this.defaultReferenceDirectives.getDefaultLocationValue();
  }

  public boolean hasExplicitlySpecifiedReferenceType()
  {
    return this.hasExplicitlySpecifiedReferenceType;
  }

  public void setExplicitlySpecifiedReferenceType(ReferenceType referenceType)
  {
    this.explicitlySpecifiedReferenceType = referenceType;
    this.hasExplicitlySpecifiedOptions = true;
    this.hasExplicitlySpecifiedReferenceType = true;
  }

  public ReferenceType getActualReferenceType()
  {
    return hasExplicitlySpecifiedReferenceType() ?
      this.explicitlySpecifiedReferenceType :
      this.defaultReferenceDirectives.getDefaultReferenceType();
  }

  public boolean hasExplicitlySpecifiedDefaultLocationValue()
  {
    return this.hasExplicitlySpecifiedDefaultLocationValue;
  }

  public void setExplicitlySpecifiedDefaultLocationValue(String locationValue)
  {
    this.hasExplicitlySpecifiedOptions = true;
    this.hasExplicitlySpecifiedDefaultLocationValue = true;
    this.explicitlySpecifiedDefaultLocationValue = locationValue;
  }

  public String getActualDefaultLocationValue()
  {
    return hasExplicitlySpecifiedDefaultLocationValue() ?
      this.explicitlySpecifiedDefaultLocationValue :
      this.defaultReferenceDirectives.getDefaultLocationValue();
  }

  public boolean hasExplicitlySpecifiedDefaultLiteral()
  {
    return this.hasExplicitlySpecifiedDefaultLiteral;
  }

  public void setExplicitlySpecifiedDefaultLiteral(String literal)
  {
    this.hasExplicitlySpecifiedOptions = true;
    this.hasExplicitlySpecifiedDefaultLiteral = true;
    this.explicitlySpecifiedDefaultLiteral = literal;
  }

  public String getActualDefaultLiteral()
  {
    return hasExplicitlySpecifiedDefaultLiteral() ?
      this.explicitlySpecifiedDefaultLiteral :
      this.defaultReferenceDirectives.getDefaultLiteral();
  }

  public boolean hasExplicitlySpecifiedShiftDirective()
  {
    return this.hasExplicitlySpecifiedShiftDirective;
  }

  public void setHasExplicitlySpecifiedShiftDirective(int shiftDirective)
  {
    this.hasExplicitlySpecifiedOptions = true;
    this.hasExplicitlySpecifiedShiftDirective = true;
    this.explicitlySpecifiedShiftDirective = shiftDirective;
  }

  public int getActualShiftDirective()
  {
    return hasExplicitlySpecifiedShiftDirective() ?
      this.explicitlySpecifiedShiftDirective :
      this.defaultReferenceDirectives.getDefaultShiftDirective();
  }

  public boolean hasExplicitlySpecifiedEmptyLocationDirective()
  {
    return this.hasExplicitlySpecifiedEmptyLocationDirective;
  }

  public void setHasExplicitlySpecifiedEmptyLocationDirective(int emptyLocationDirective)
  {
    this.hasExplicitlySpecifiedOptions = true;
    this.hasExplicitlySpecifiedEmptyLocationDirective = true;
    this.explicitlySpecifiedEmptyLocationDirective = emptyLocationDirective;
  }

  public int getActualEmptyLocationDirective()
  {
    return hasExplicitlySpecifiedEmptyLocationDirective() ?
      this.explicitlySpecifiedEmptyLocationDirective :
      this.defaultReferenceDirectives.getDefaultEmptyLocationDirective();
  }

  public boolean hasExplicitlySpecifiedEmptyLiteralDirective()
  {
    return this.hasExplicitlySpecifiedEmptyLiteralDirective;
  }

  public void setHasExplicitlySpecifiedEmptyLiteralDirective(int emptyLiteralDirective)
  {
    this.hasExplicitlySpecifiedOptions = true;
    this.hasExplicitlySpecifiedEmptyLiteralDirective = true;
    this.explicitlySpecifiedEmptyLiteralDirective = emptyLiteralDirective;
  }

  public int getActualEmptyLiteralDirective()
  {
    return hasExplicitlySpecifiedEmptyLiteralDirective() ?
      this.explicitlySpecifiedEmptyLiteralDirective :
      this.defaultReferenceDirectives.getDefaultEmptyLiteralDirective();
  }

  public SpreadsheetLocation getShiftedLocation()
  {
    return this.shiftedLocation;
  }

  public void setShiftedLocation(SpreadsheetLocation shiftedLocation)
  {
    this.shiftedLocation = shiftedLocation;
  }
}