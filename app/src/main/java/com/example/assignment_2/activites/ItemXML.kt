package com.example.assignment_2.activites


import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(namespace = "ITEM")
data class ItemXML(
    @JacksonXmlProperty(localName = "ITEMTYPE") val itemType: String,
    @JacksonXmlProperty(localName = "ITEMID") val itemId: String,
    @JacksonXmlProperty(localName = "QTY") val quantity: Int,
    @JacksonXmlProperty(localName = "COLOR") val color: Int,
    @JacksonXmlProperty(localName = "EXTRA") val extra: String,
    @JacksonXmlProperty(localName = "ALTERNATE") val alternate: String,
    @JacksonXmlProperty(localName = "MATCHID") val matchId: Int,
    @JacksonXmlProperty(localName = "COUNTERPART") val counterPart: String
)