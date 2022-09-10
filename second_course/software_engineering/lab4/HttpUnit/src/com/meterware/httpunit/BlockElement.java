package com.meterware.httpunit;
/********************************************************************************************************************
 * $Id: BlockElement.java,v 1.3 2004/09/29 17:15:24 russgold Exp $
 *
 * Copyright (c) 2004, Russell Gold
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions
 * of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 *
 *******************************************************************************************************************/
import org.w3c.dom.Node;

import java.net.URL;

import com.meterware.httpunit.scripting.ScriptableDelegate;

/**
 * Represents a block-level element such as a paragraph or table cell, which can contain other elements.
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 *
 * @since 1.6
 **/
abstract public class BlockElement extends ParsedHTML implements HTMLSegment, HTMLElement {


    private ScriptableDelegate _scriptable;
    private Node               _node;


    /**
     * Returns the text value of this block.
     **/
    public String getText() {
        if (_node.getNodeType() == Node.TEXT_NODE) {
            return _node.getNodeValue().trim();
        } else if (_node == null || !_node.hasChildNodes()) {
            return "";
        } else {
            return NodeUtils.asText( _node.getChildNodes() ).trim();
        }
    }


    /**
     * Returns the tag for this block.
     */
    public String getTagName() {
        return _node == null ? "p" : _node.getNodeName();
    }


    /**
     * Returns a copy of the domain object model associated with this HTML segment.
     **/
    public Node getDOM() {
        return super.getDOM();
    }


//-------------------------------- HTMLElement methods ---------------------------------------


    /**
     * Returns the ID associated with this element. IDs are unique throughout the HTML document.
     **/
    public String getID() {
        return getAttribute( "id" );
    }


    /**
     * Returns the class attribute associated with this element.
     */
    public String getClassName() {
        return getAttribute( "class" );
    }


    /**
     * Returns the name associated with this element.
     **/
    public String getName() {
        return getAttribute( "name" );
    }


    /**
     * Returns the title associated with this element.
     **/
    public String getTitle() {
        return getAttribute( "title" );
    }


    /**
     * Returns the delegate which supports scripting this element.
     */
    public ScriptableDelegate getScriptableDelegate() {
        if (_scriptable == null) {
            _scriptable = new HTMLElementScriptable( this );
            _scriptable.setScriptEngine( getResponse().getScriptableObject().getDocument().getScriptEngine( _scriptable ) );
        }
        return _scriptable;
    }


    public String getAttribute( final String name ) {
        return NodeUtils.getNodeAttribute( _node, name );
    }


    /**
     * Returns true if this element may have an attribute with the specified name.
     */
    public boolean isSupportedAttribute( String name ) {
        return false;
    }


//----------------------------------------------- Object methods -------------------------------------------------------


    public boolean equals( Object obj ) {
        return getClass().equals( obj.getClass() ) && equals( (BlockElement) obj );
    }


    private boolean equals( BlockElement block ) {
        return _node.equals( block._node );
    }


    public int hashCode() {
        return _node.hashCode();
    }


//------------------------------------- protected members --------------------------------------------------------------


    protected BlockElement( WebResponse response, FrameSelector frame, URL baseURL, String baseTarget, Node rootNode, String characterSet ) {
        super( response, frame, baseURL, baseTarget, rootNode, characterSet );
        _node = rootNode;
    }


    protected int getAttributeValue( Node node, String attributeName, int defaultValue ) {
        return NodeUtils.getAttributeValue( node, attributeName, defaultValue );
    }
}
