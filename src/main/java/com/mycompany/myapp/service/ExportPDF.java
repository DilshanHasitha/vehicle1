package com.mycompany.myapp.service;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.base.JRBasePrintPage;
import net.sf.jasperreports.engine.base.JRBasePrintText;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.*;
import net.sf.jasperreports.engine.type.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ExportPDF {

    public byte[] pdfCreator(Set<?> details, String xmlName) throws IOException, JRException {
        JasperDesign jasperDesign = new JasperDesign();
        jasperDesign.setName("The dynamically generated report");
        jasperDesign.setPageWidth(595);
        jasperDesign.setPageHeight(842);
        //        jasperDesign.setOrientation(OrientationEnum.LANDSCAPE);
        jasperDesign.setColumnWidth(535);
        //        jasperDesign.setColumnSpacing(2);
        jasperDesign.setTitleNewPage(false);
        jasperDesign.setFloatColumnFooter(false);
        jasperDesign.setSummaryNewPage(false);

        JRDesignBand band = new JRDesignBand();
        band.setHeight(750);

        JRDesignExpression expression = new JRDesignExpression();
        String imgPath = "https://s3-generic-uploads.s3.ap-south-1.amazonaws.com/ADPanicBuying1590308750.jpeg";
        //        expression.setText("\"C:/Users/dilsh/Downloads/1.png\"");
        expression.setText("\"https://wikunum-lite-generic.s3.ap-south-1.amazonaws.com/storeImages/ADPanicBuying1665638023.jpeg\"");

        JRDesignExpression expression1 = new JRDesignExpression();
        expression1.setText("\"https://wikunum-lite-generic.s3.ap-south-1.amazonaws.com/storeImages/MULTIMultiUpload_1655570694.jpeg\"");

        JRDesignImage image = new JRDesignImage(jasperDesign);
        image.setX(0);
        image.setY(0);
        image.setWidth(545);
        image.setHeight(750);
        image.setScaleImage(ScaleImageEnum.FILL_FRAME);
        image.setExpression(expression);
        //
        //        JRDesignImage image1 = new JRDesignImage(jasperDesign);
        //        image1.setX(0);
        //        image1.setY(0);
        //        image1.setWidth(200);
        //        image1.setHeight(200);
        //        image1.setScaleImage(ScaleImageEnum.FILL_FRAME);
        //        image1.setExpression(expression);
        //
        //        JRDesignImage image2 = new JRDesignImage(jasperDesign);
        //        image2.setX(70);
        //        image2.setY(70);
        //        image2.setWidth(200);
        //        image2.setHeight(200);
        //        image2.setScaleImage(ScaleImageEnum.FILL_FRAME);
        //        image2.setExpression(expression1);

        JRDesignFrame frame = new JRDesignFrame();
        JRDesignField field = new JRDesignField();
        //        field.setName("merchantCode");
        //        field.setValueClass(String.class);
        //        jasperDesign.addField(field);
        frame.setX(0);
        frame.setY(5);
        frame.setWidth(595);
        frame.setHeight(842);
        frame.setForecolor(Color.RED);
        frame.setBackcolor(Color.RED);
        frame.setMode(ModeEnum.OPAQUE);

        JRDesignStaticText staticText = new JRDesignStaticText();
        staticText.setX(0);
        staticText.setY(0);
        staticText.setWidth(55);
        staticText.setHeight(15);
        staticText.setForecolor(Color.BLUE);
        //        staticText.setBackcolor(Color.GREEN);
        staticText.setMode(ModeEnum.OPAQUE);
        staticText.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        staticText.setText("ID");
        frame.addElement(staticText);

        // Add image to Detail band
        band.addElement(0, image);
        band.addElement(1, staticText);
        //        band.addElement(1,image1);
        //        band.addElement(2,image2);

        //        jasperDesign.setTitle(band);

        //        JRDesignTextField cellTextField = new JRDesignTextField();
        //        cellTextField.setX(10);
        //        cellTextField.setY(0);
        //        cellTextField.setWidth(100);
        //        cellTextField.setHeight(band.getHeight());
        //        cellTextField.setPrintRepeatedValues(true);
        //        cellTextField.setPositionType(PositionTypeEnum.FIX_RELATIVE_TO_TOP);
        //        cellTextField.setFontSize(24F);
        //        cellTextField.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        //        cellTextField.setStretchType(StretchTypeEnum.CONTAINER_HEIGHT);
        //        cellTextField.setPositionType(PositionTypeEnum.FLOAT);

        //        JRDesignVariable variable = new JRDesignVariable();
        //        variable.setName("TestValue");
        //        variable.setValueClass(java.lang.String.class);
        //        jasperDesign.addVariable(variable);
        //
        //        JRDesignExpression cellExpression = new JRDesignExpression("TestValue");
        //        cellTextField.setExpression(cellExpression);
        //        band.addElement(cellTextField);
        ((JRDesignSection) jasperDesign.getDetailSection()).addBand(band);

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(details);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "AlphaDevs");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);
        return data;
    }
    //        try {
    //            Resource resource = new ClassPathResource(xmlName);
    //            File file=resource.getFile();
    //            JasperReport jasperReport= JasperCompileManager.compileReport(file.getAbsolutePath());
    //            JRBeanCollectionDataSource dataSource=new JRBeanCollectionDataSource(details);
    //            Map<String,Object> parameters =new HashMap<>();
    //            parameters.put("createdBy","AlphaDevs");
    //            JasperPrint jasperPrint= JasperFillManager.fillReport(jasperReport,parameters,dataSource);
    ////            JasperPrint jasperPrint= JasperFillManager.getInstance(jasperReportsContext).fill(jasperReport,parameters,dataSource);
    ////            for ( JRPrintElement element : jasperPrint.getPages().get(0).getElements()) {
    ////                element.setY(element.getY()+10);
    ////            }
    ////            JRDesignBand band = new JRDesignBand();
    ////            JRDesignTextField textField = new JRDesignTextField();
    ////            JRDesignExpression expression = new JRDesignExpression();
    ////            expression.setValueClass(String.class);
    ////            expression.setText("FFFF");
    ////            textField.setX(0);
    ////            textField.setY(0);
    ////            textField.setExpression(expression);
    ////            band.addElement(textField);
    //            JRPrintPage jrPrintPage=new JRBasePrintPage();
    //            JRBasePrintText textPageNumber = new JRBasePrintText(jasperPrint.getDefaultStyleProvider());
    //            textPageNumber.setX(300);
    //            textPageNumber.setY(150);
    //            textPageNumber.setWidth(204);
    //            textPageNumber.setHeight(102);
    //            System.out.println(textPageNumber.getFontsize());
    //            textPageNumber.setFontSize(14F);
    //            textPageNumber.setText("aaaaaaaaa");
    //
    //
    //            jasperPrint.addPage(jrPrintPage);
    //            jasperPrint.getPages().get(1).addElement(textPageNumber);
    //            byte[] data =JasperExportManager.exportReportToPdf(jasperPrint);
    //            return  data;
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //            return null;
    //        }
    //
    //
    //    }

}
