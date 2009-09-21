/*
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Apr 22, 2002
 * Time: 5:51:22 PM
 * To change template for new class use
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package com.intellij.openapi.editor.impl;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.FoldRegion;
import com.intellij.openapi.editor.FoldingGroup;
import com.intellij.openapi.editor.ex.DocumentEx;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FoldRegionImpl extends RangeMarkerImpl implements FoldRegion {
  private boolean myIsExpanded;
  private final Editor myEditor;
  private final String myPlaceholderText;
  private final FoldingGroup myGroup;

  public FoldRegionImpl(Editor editor, int startOffset, int endOffset, String placeholder, FoldingGroup group) {
    super((DocumentEx)editor.getDocument(), startOffset, endOffset);
    myGroup = group;
    myIsExpanded = true;
    myEditor = editor;
    myPlaceholderText = placeholder;
  }

  public boolean isExpanded() {
    return myIsExpanded;
  }

  public void setExpanded(boolean expanded) {
    FoldingModelImpl foldingModel = (FoldingModelImpl)myEditor.getFoldingModel();
    if (myGroup == null) {
      doSetExpanded(expanded, foldingModel, this);
    } else {
      for (final FoldRegion region : foldingModel.getGroupedRegions(myGroup)) {
        doSetExpanded(expanded, foldingModel, region);
      }
    }
  }

  private static void doSetExpanded(boolean expanded, FoldingModelImpl foldingModel, FoldRegion region) {
    if (expanded) {
      foldingModel.expandFoldRegion(region);
    }
    else{
      foldingModel.collapseFoldRegion(region);
    }
  }

  public boolean isValid() {
    return super.isValid() && getStartOffset() + 1 < getEndOffset();
  }

  public void setExpandedInternal(boolean toExpand) {
    myIsExpanded = toExpand;
  }

  @NotNull
  public String getPlaceholderText() {
    return myPlaceholderText;
  }

  public Editor getEditor() {
    return myEditor;
  }

  @Nullable
  public FoldingGroup getGroup() {
    return myGroup;
  }

  @SuppressWarnings({"HardCodedStringLiteral"})
  public String toString() {
    return "FoldRegion (" + getStartOffset() + ":" + getEndOffset() + ")";
  }
}
