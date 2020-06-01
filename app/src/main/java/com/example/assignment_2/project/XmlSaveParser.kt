package com.example.assignment_2.project

import com.example.assignment_2.model.InventoriesParts
import org.w3c.dom.Document
import org.w3c.dom.Element
import java.io.File
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

class XmlSaveParser {
    fun saveXML(bricks: List<ItemModel>, file:File) {
        val docBuilder: DocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        val doc: Document = docBuilder.newDocument()

        val rootElement: Element = doc.createElement("INVENTORY")

        val bricksFiltered = bricks.filter { item -> item.amount != item.maxAmount }

        for(brick in bricksFiltered) {
            val itemElement:Element = doc.createElement("ITEM")
            val itemType:Element = doc.createElement("ITEMTYPE")
            val itemID:Element = doc.createElement("COLOR")
            val color:Element = doc.createElement("COLOR")
            val qtyFilled:Element = doc.createElement("QTYFILLED")
            itemType.appendChild(doc.createTextNode(brick.type))
            itemID.appendChild(doc.createTextNode(brick.code))
            color.appendChild(doc.createTextNode(brick.color))
            qtyFilled.appendChild(doc.createTextNode(brick.amount.toString()))

            itemElement.appendChild(itemType)
            itemElement.appendChild(itemID)
            itemElement.appendChild(color)
            itemElement.appendChild(qtyFilled)

            rootElement.appendChild(itemElement)
        }
        val transformer: Transformer = TransformerFactory.newInstance().newTransformer()
        doc.appendChild(rootElement)

        transformer.setOutputProperty(OutputKeys.INDENT, "yes")
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2")
        transformer.transform(DOMSource(doc), StreamResult(file))
    }
}