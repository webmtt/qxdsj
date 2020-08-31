package org.cimiss2.dwp.util;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.*;

public class ReadXml {
    public static List readStringXml(String xml) throws Exception {
        Document doc = null;
        List fileName = new ArrayList<>();
        doc = DocumentHelper.parseText(xml); // 将字符串转为XML
        Element rootElt = doc.getRootElement(); // 获取根节点
        String docName = null;
        String toalName = null;
//            Element contactElem = rootElt.element("MsgContent").element("DataList").element("DataInfo").element("DataFileList");//首先要知道自己要操作的节点。
//            Iterator<Node> it = contactElem.elementIterator();
//            String toalName = null;
//            while (it.hasNext()) {
//                Node node = it.next();
//                if (node instanceof Element) {
//                    Element e1 = (Element) node;
//                    toalName = docName + e1.element("FileName").getTextTrim().substring(e1.element("FileName").getTextTrim().indexOf("~") + 1,e1.element("FileName").getTextTrim().length());
//                    fileName.add(toalName);
//                }
//            }
//
//            for(int i = 0;i < fileName.size();i++){
//                System.out.println(fileName.get(i));
//            }

        Element contactElem = rootElt.element("MsgContent");//首先要知道自己要操作的节点。
        Iterator<Node> it = contactElem.elementIterator();
        while (it.hasNext()) {
            Node node = it.next();
            if (!node.getName().equals("RequestID")) {
                if (node instanceof Element) {
                    Element e1 = (Element) node;
                    docName = e1.element("DataInfo").element("DirLoc").getTextTrim();
                    Element contactElems = e1.element("DataInfo").element("DataFileList");
                    Iterator<Node> its = contactElems.elementIterator();
                    while (its.hasNext()) {
                        Node nodes = its.next();
                        if (nodes instanceof Element) {
                            Element e2 = (Element) nodes;
                            toalName = docName + e2.element("FileName").getTextTrim().substring(e2.element("FileName").getTextTrim().indexOf("~") + 1, e2.element("FileName").getTextTrim().length());
                            fileName.add(toalName);
                        }
                    }
                }
            }

        }
        return fileName;
    }

    public static void main(String[] args) throws Exception {
        String xmlString = "<?xml version=\"1.0\"?>\n" +
                "<CTS_DPC_MSG>\n" +
                "  <MsgHeader>\n" +
                "    <MsgType>PROCESS</MsgType>\n" +
                "    <SendTime>2020-06-23 06:08:02</SendTime>\n" +
                "    <Sender>CTS</Sender>\n" +
                "    <Reciever>DPC</Reciever>\n" +
                "  </MsgHeader>\n" +
                "  <MsgContent>\n" +
                "    <RequestID>1111</RequestID>\n" +
                "    <DataList>\n" +
                "      <DataInfo>\n" +
                "        <DataType>A.0001.0028.R001</DataType>\n" +
                "        <DPri>1</DPri>\n" +
                "        <DirLoc>C:\\Users\\piesat\\Desktop\\近地面通量\\20200601\\cast\\rada\\p_dor\\Z9474\\</DirLoc>\n" +
                "        <DataFileList>\n" +
                "          <FileInfo>\n" +
                "            <FileName>A.0001.0028.R001~PBL_FLUX_S_54102_202005.TXT</FileName>\n" +
                "            <FileSize>2049</FileSize>\n" +
                "          </FileInfo>\n" +
//                "          <FileInfo>\n" +
//                "            <FileName>A.0001.0028.R001~Z_SURF_C_BEHT-REG_20200623060733_O_AWS_FTM_PQC.txt</FileName>\n" +
//                "            <FileSize>1368</FileSize>\n" +
//                "          </FileInfo>\n" +
                "        </DataFileList>\n" +
                "      </DataInfo>\n" +
                "    </DataList>\n" +
//                "    <DataList>\n" +
//                "      <DataInfo>\n" +
//                "        <DataType>A.0001.0029.R001</DataType>\n" +
//                "        <DPri>1</DPri>\n" +
//                "        <DirLoc>/space/cimiss_BEHT/run/azone/input1//A/2020062306/</DirLoc>\n" +
//                "        <DataFileList>\n" +
//                "          <FileInfo>\n" +
//                "            <FileName>A.0001.0029.R001~Z_SURF_C_BEHT-REG_20200623060737_O_AWS-PRF_FTM_PQC.txt</FileName>\n" +
//                "            <FileSize>504</FileSize>\n" +
//                "          </FileInfo>\n" +
//                "          <FileInfo>\n" +
//                "            <FileName>A.0001.0029.R001~Z_SURF_C_BEHT-REG_20200623060732_O_AWS-PRF_FTM_PQC.txt</FileName>\n" +
//                "            <FileSize>255</FileSize>\n" +
//                "          </FileInfo>\n" +
//                "        </DataFileList>\n" +
//                "      </DataInfo>\n" +
//                "    </DataList>\n" +
                "  </MsgContent>\n" +
                "</CTS_DPC_MSG>";
       List list =  readStringXml(xmlString);
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
    }

}
