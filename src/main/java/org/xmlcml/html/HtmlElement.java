/**
 *    Copyright 2011 Peter Murray-Rust et. al.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

    package org.xmlcml.html;

import java.io.IOException;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Nodes;

import org.apache.log4j.Logger;

import org.xmlcml.cml.base.CMLConstants;
import org.xmlcml.cml.base.CMLUtil;

/*
 Legend: Optional, Forbidden, Empty, Deprecated, Loose DTD, Frameset DTD
Name 	Start Tag 	End Tag 	Empty 	Depr. 	DTD 	Description
A 	  	  	  	  	  	anchor
ABBR 	  	  	  	  	  	abbreviated form (e.g., WWW, HTTP, etc.)
ACRONYM 	  	  	  	  	  	 
ADDRESS 	  	  	  	  	  	information on author
APPLET 	  	  	  	D 	L 	Java applet
AREA 	  	F 	E 	  	  	client-side image map area
B 	  	  	  	  	  	bold text style
BASE 	  	F 	E 	  	  	document base URI
BASEFONT 	  	F 	E 	D 	L 	base font size
BDO 	  	  	  	  	  	I18N BiDi over-ride
BIG 	  	  	  	  	  	large text style
BLOCKQUOTE 	  	  	  	  	  	long quotation
BODY 	O 	O 	  	  	  	document body
BR 	  	F 	E 	  	  	forced line break
BUTTON 	  	  	  	  	  	push button
CAPTION 	  	  	  	  	  	table caption
CENTER 	  	  	  	D 	L 	shorthand for DIV align=center
CITE 	  	  	  	  	  	citation
CODE 	  	  	  	  	  	computer code fragment
COL 	  	F 	E 	  	  	table column
COLGROUP 	  	O 	  	  	  	table column group
DD 	  	O 	  	  	  	definition description
DEL 	  	  	  	  	  	deleted text
DFN 	  	  	  	  	  	instance definition
DIR 	  	  	  	D 	L 	directory list
DIV 	  	  	  	  	  	generic language/style container
DL 	  	  	  	  	  	definition list
DT 	  	O 	  	  	  	definition term
EM 	  	  	  	  	  	emphasis
FIELDSET 	  	  	  	  	  	form control group
FONT 	  	  	  	D 	L 	local change to font
FORM 	  	  	  	  	  	interactive form
FRAME 	  	F 	E 	  	F 	subwindow
FRAMESET 	  	  	  	  	F 	window subdivision
H1 	  	  	  	  	  	heading
H2 	  	  	  	  	  	heading
H3 	  	  	  	  	  	heading
H4 	  	  	  	  	  	heading
H5 	  	  	  	  	  	heading
H6 	  	  	  	  	  	heading
HEAD 	O 	O 	  	  	  	document head
HR 	  	F 	E 	  	  	horizontal rule
HTML 	O 	O 	  	  	  	document root element
I 	  	  	  	  	  	italic text style
IFRAME 	  	  	  	  	L 	inline subwindow
IMG 	  	F 	E 	  	  	Embedded image
INPUT 	  	F 	E 	  	  	form control
INS 	  	  	  	  	  	inserted text
ISINDEX 	  	F 	E 	D 	L 	single line prompt
KBD 	  	  	  	  	  	text to be entered by the user
LABEL 	  	  	  	  	  	form field label text
LEGEND 	  	  	  	  	  	fieldset legend
LI 	  	O 	  	  	  	list item
LINK 	  	F 	E 	  	  	a media-independent link
MAP 	  	  	  	  	  	client-side image map
MENU 	  	  	  	D 	L 	menu list
META 	  	F 	E 	  	  	generic metainformation
NOFRAMES 	  	  	  	  	F 	alternate content container for non frame-based rendering
NOSCRIPT 	  	  	  	  	  	alternate content container for non script-based rendering
OBJECT 	  	  	  	  	  	generic embedded object
OL 	  	  	  	  	  	ordered list
OPTGROUP 	  	  	  	  	  	option group
OPTION 	  	O 	  	  	  	selectable choice
P 	  	O 	  	  	  	paragraph
PARAM 	  	F 	E 	  	  	named property value
PRE 	  	  	  	  	  	preformatted text
Q 	  	  	  	  	  	short inline quotation
S 	  	  	  	D 	L 	strike-through text style
SAMP 	  	  	  	  	  	sample program output, scripts, etc.
SCRIPT 	  	  	  	  	  	script statements
SELECT 	  	  	  	  	  	option selector
SMALL 	  	  	  	  	  	small text style
SPAN 	  	  	  	  	  	generic language/style container
STRIKE 	  	  	  	D 	L 	strike-through text
STRONG 	  	  	  	  	  	strong emphasis
STYLE 	  	  	  	  	  	style info
SUB 	  	  	  	  	  	subscript
SUP 	  	  	  	  	  	superscript
TABLE 	  	  	  	  	  	 
TBODY 	O 	O 	  	  	  	table body
TD 	  	O 	  	  	  	table data cell
TEXTAREA 	  	  	  	  	  	multi-line text field
TFOOT 	  	O 	  	  	  	table footer
TH 	  	O 	  	  	  	table header cell
THEAD 	  	O 	  	  	  	table header
TITLE 	  	  	  	  	  	document title
TR 	  	O 	  	  	  	table row
TT 	  	  	  	  	  	teletype or monospaced text style
U 	  	  	  	D 	L 	underlined text style
UL 	  	  	  	  	  	unordered list
VAR 	  	  	  	  	  	instance of a variable or program argument

 */
/** base class for lightweight generic HTML element.
 * no checking - i.e. can take any name or attributes
 * @author pm286
 *
 */
public abstract class HtmlElement extends Element implements CMLConstants {
	private static final String ID = "id";
	private final static Logger LOG = Logger.getLogger(HtmlElement.class);

	public static String[] tags = {
		"A", 
		"ABBR", 
		"ACRONYM", 
		"ADDRESS", 
		"APPLET", 
		"AREA", 
		"B", 
		"BASE", 
		"BASEFONT", 
		"BDO", 
		"BIG", 
		"BLOCKQUOTE", 
		"BODY", 
		"BR", 
		"BUTTON", 
		"CAPTION", 
		"CENTER", 
		"CITE", 
		"CODE", 
		"COL", 
		"COLGROUP", 
		"DD", 
		"DEL", 
		"DFN", 
		"DIR", 
		"DIV", 
		"DL", 
		"DT", 
		"EM", 
		"FIELDSET", 
		"FONT", 
		"FORM", 
		"FRAME", 
		"FRAMESET", 
		"H1", 
		"H2", 
		"H3", 
		"H4", 
		"H5", 
		"H6", 
		"HEAD", 
		"HR", 
		"HTML", 
		"I", 
		"IFRAME", 
		"IMG", 
		"INPUT", 
		"INS", 
		"ISINDEX", 
		"KBD", 
		"LABEL", 
		"LEGEND", 
		"LI", 
		"LINK", 
		"MAP", 
		"MENU", 
		"META", 
		"NOFRAMES", 
		"NOSCRIPT", 
		"OBJECT", 
		"OL", 
		"OPTGROUP", 
		"OPTION", 
		"P", 
		"PARAM", 
		"PRE", 
		"Q", 
		"S", 
		"SAMP", 
		"SCRIPT", 
		"SELECT", 
		"SMALL", 
		"SPAN", 
		"STRIKE", 
		"STRONG", 
		"STYLE", 
		"SUB", 
		"SUP", 
		"TABLE", 
		"TBODY", 
		"TD", 
		"TEXTAREA", 
		"TFOOT", 
		"TH", 
		"THEAD", 
		"TITLE", 
		"TR", 
		"TT", 
		"U", 
		"UL", 
		"VAR", 
	};
	public static Set<String> TAGSET;
	static {
		TAGSET = new HashSet<String>();
		for (String tag : tags) {
			TAGSET.add(tag);
		}
	};
	
	public enum Target {
		bottom,
		menu,
		separate;
	};
	/** constructor.
	 * 
	 * @param name
	 * @param namespace
	 */
	public HtmlElement(String name) {
		super(name, XHTML_NS);
	}
	
	public static HtmlElement create(Element element) {
		HtmlElement htmlElement = null;
		String tag = element.getLocalName();
		if(HtmlA.TAG.equalsIgnoreCase(tag)) {
			htmlElement = new HtmlA();
		} else if(HtmlB.TAG.equalsIgnoreCase(tag)) {
			htmlElement = new HtmlB();
		} else if(HtmlBig.TAG.equalsIgnoreCase(tag)) {
			htmlElement = new HtmlBig();
		} else if(HtmlBody.TAG.equalsIgnoreCase(tag)) {
			htmlElement = new HtmlBody();
		} else if(HtmlBr.TAG.equalsIgnoreCase(tag)) {
			htmlElement = new HtmlBr();
		} else if(HtmlCaption.TAG.equalsIgnoreCase(tag)) {
			htmlElement = new HtmlCaption();
		} else if(HtmlDiv.TAG.equalsIgnoreCase(tag)) {
			htmlElement = new HtmlDiv();
		} else if(HtmlFrame.TAG.equalsIgnoreCase(tag)) {
			htmlElement = new HtmlFrame();
		} else if(HtmlFrameset.TAG.equalsIgnoreCase(tag)) {
			htmlElement = new HtmlFrameset();
		} else if(HtmlH1.TAG.equalsIgnoreCase(tag)) {
			htmlElement = new HtmlH1();
		} else if(HtmlH2.TAG.equalsIgnoreCase(tag)) {
			htmlElement = new HtmlH2();
		} else if(HtmlH3.TAG.equalsIgnoreCase(tag)) {
			htmlElement = new HtmlH3();
		} else if(HtmlHead.TAG.equalsIgnoreCase(tag)) {
			htmlElement = new HtmlHead();
		} else if(HtmlHr.TAG.equalsIgnoreCase(tag)) {
			htmlElement = new HtmlHr();
		} else if(HtmlHtml.TAG.equalsIgnoreCase(tag)) {
			htmlElement = new HtmlHtml();
		} else if(HtmlI.TAG.equalsIgnoreCase(tag)) {
			htmlElement = new HtmlI();
		} else if(HtmlImg.TAG.equalsIgnoreCase(tag)) {
			htmlElement = new HtmlImg();
		} else if(HtmlLi.TAG.equalsIgnoreCase(tag)) {
			htmlElement = new HtmlLi();
		} else if(HtmlLink.TAG.equalsIgnoreCase(tag)) {
			htmlElement = new HtmlLink();
		} else if(HtmlMeta.TAG.equalsIgnoreCase(tag)) {
			htmlElement = new HtmlMeta();
		} else if(HtmlOl.TAG.equalsIgnoreCase(tag)) {
			htmlElement = new HtmlOl();
		} else if(HtmlP.TAG.equalsIgnoreCase(tag)) {
			htmlElement = new HtmlP();
		} else if(HtmlS.TAG.equalsIgnoreCase(tag)) {
			htmlElement = new HtmlS();
		} else if(HtmlSmall.TAG.equalsIgnoreCase(tag)) {
			htmlElement = new HtmlSmall();
		} else if(HtmlSpan.TAG.equalsIgnoreCase(tag)) {
			htmlElement = new HtmlSpan();
		} else if(HtmlStrong.TAG.equalsIgnoreCase(tag)) {
			htmlElement = new HtmlStrong();
		} else if(HtmlStyle.TAG.equalsIgnoreCase(tag)) {
			htmlElement = new HtmlStyle();
		} else if(HtmlSub.TAG.equalsIgnoreCase(tag)) {
			htmlElement = new HtmlSub();
		} else if(HtmlSup.TAG.equalsIgnoreCase(tag)) {
			htmlElement = new HtmlSup();
		} else if(HtmlTable.TAG.equalsIgnoreCase(tag)) {
			htmlElement = new HtmlTable();
		} else if(HtmlTbody.TAG.equalsIgnoreCase(tag)) {
			htmlElement = new HtmlTbody();
		} else if(HtmlTfoot.TAG.equalsIgnoreCase(tag)) {
			htmlElement = new HtmlTfoot();
		} else if(HtmlThead.TAG.equalsIgnoreCase(tag)) {
			htmlElement = new HtmlThead();
		} else if(HtmlTd.TAG.equalsIgnoreCase(tag)) {
			htmlElement = new HtmlTd();
		} else if(HtmlTh.TAG.equalsIgnoreCase(tag)) {
			htmlElement = new HtmlTh();
		} else if(HtmlTr.TAG.equalsIgnoreCase(tag)) {
			htmlElement = new HtmlTr();
		} else if(HtmlTt.TAG.equalsIgnoreCase(tag)) {
			htmlElement = new HtmlTt();
		} else if(HtmlUl.TAG.equalsIgnoreCase(tag)) {
			htmlElement = new HtmlUl();
		} else if (TAGSET.contains(tag.toUpperCase())) {
			htmlElement = new HtmlGeneric(tag.toLowerCase());
		} else {
			throw new RuntimeException("Unknown html tag "+tag);
		}
		CMLUtil.copyAttributes(element, htmlElement);
		for (int i = 0; i < element.getChildCount(); i++) {
			Node child = element.getChild(i);
			if (child instanceof Element) {
				HtmlElement htmlChild = HtmlElement.create((Element)child);
				htmlElement.appendChild(htmlChild);
			} else {
				htmlElement.appendChild(child.copy());
			}
		}
		return htmlElement;
		
	}
	
	public void setAttribute(String name, String value) {
		this.addAttribute(new Attribute(name, value));
	}

	public void setContent(String content) {
		this.appendChild(content);
	}
	
	public void setClass(String value) {
		this.setAttribute("class", value);
	}

	public void setId(String value) {
		this.setAttribute(ID, value);
	}

	public void setName(String value) {
		this.setAttribute("name", value);
	}

	public void output(OutputStream os) throws IOException {
		CMLUtil.debug(this, os, 1);
	}

	public void debug(String msg) {
		CMLUtil.debug(this, msg);
	}

	public void setValue(String value) {
		this.removeChildren();
		this.appendChild(value);
	}

	public String getId() {
		return this.getAttributeValue(ID);
	}

	public static List<HtmlElement> getSelfOrDescendants(HtmlElement root, String tag) {
		tag = tag.toLowerCase();
		Nodes nodes = root.query(".//*[local-name()='"+tag+"'");
		List<HtmlElement> elements = new ArrayList<HtmlElement>();
		for (int i = 0; i < nodes.size(); i++) {
			elements.add((HtmlElement)nodes.get(i));
		}
		return elements;
	}

	public static HtmlElement getSingleSelfOrDescendant(HtmlElement root, String tag) {
		List<HtmlElement> elements = getSelfOrDescendants(root, tag);
		return (elements.size() != 1) ? null : elements.get(0);
	}

	public static List<HtmlElement> getChildElements(HtmlElement root, String tag) {
		tag = tag.toLowerCase();
		Nodes nodes = root.query("./*[local-name()='"+tag+"']");
		List<HtmlElement> elements = new ArrayList<HtmlElement>();
		for (int i = 0; i < nodes.size(); i++) {
			elements.add((HtmlElement)nodes.get(i));
		}
		return elements;
	}

	public static HtmlElement getSingleChildElement(HtmlElement root, String tag) {
		List<HtmlElement> elements = getChildElements(root, tag);
		return (elements.size() != 1) ? null : elements.get(0);
	}



}
