package com.example.main;

import com.example.model.Erdekeltseg;
import com.example.model.HelyrajziSzam;
import com.example.model.MuvelesiAg;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaborsornyei on 16. 02. 26..
 */
public class Main {
    public static void main(String[] args) throws IOException, InvalidFormatException {
        String filename = "bardudvarnok.xlsx";
        InputStream is = Main.class.getClassLoader().getResourceAsStream(filename);
        Workbook wb = WorkbookFactory.create(is);
        is.close();
        Sheet ws = wb.getSheetAt(0);

        List<HelyrajziSzam> helyrajziSzamList = new ArrayList<>();


        int rowIndex = 1;
        String oldHrsz = "";
        HelyrajziSzam hrsz = null;
        while (true) {
            Row row = ws.getRow(rowIndex);
            Cell hrszCell = row.getCell(0);
            if (hrszCell.getStringCellValue().equals("Földrészlet:")) {
                break;
            }
            if (!hrszCell.getStringCellValue().equals(oldHrsz)) {
                hrsz = new HelyrajziSzam(hrszCell.getStringCellValue());
                helyrajziSzamList.add(hrsz);
                oldHrsz = hrszCell.getStringCellValue();
            }
            Cell muvAgCell = row.getCell(1, Row.RETURN_BLANK_AS_NULL);
            Cell erdTipCell = row.getCell(8, Row.RETURN_BLANK_AS_NULL);
            if (muvAgCell != null) {
                MuvelesiAg muvelesiAg = new MuvelesiAg();
                muvelesiAg.setMegnevezes(muvAgCell.getStringCellValue());
                muvelesiAg.setTerulet(row.getCell(4, Row.RETURN_BLANK_AS_NULL).getNumericCellValue());
                muvelesiAg.setKivettMegnevezes(row.getCell(6, Row.RETURN_BLANK_AS_NULL).getStringCellValue());
                hrsz.getMuvelesiAgList().add(muvelesiAg);
            } else if (erdTipCell != null) {
                Erdekeltseg erdekeltseg = new Erdekeltseg();
                if (erdTipCell.getStringCellValue().equals("tulajdonos")) {

                    erdekeltseg.setTipus(erdTipCell.getStringCellValue());
                    String erd = row.getCell(9, Row.RETURN_BLANK_AS_NULL).getStringCellValue().split("/ bej.")[0];
                    erdekeltseg.setErdekelt(erd);
                    erdekeltseg.setHanyadStr(row.getCell(10, Row.RETURN_BLANK_AS_NULL).getStringCellValue());
                    String hanyadStr = row.getCell(10, Row.RETURN_BLANK_AS_NULL).getStringCellValue();
                    String[] arany = hanyadStr.split("/");
                    double hanyad = Double.parseDouble(arany[0]) / Double.parseDouble(arany[1]);
                    erdekeltseg.setHanyad(Math.round(hanyad * 10000) / 10000.0);
                    hrsz.getErdekeltsegList().add(erdekeltseg);
                }
            }

            rowIndex++;
        }

        for (HelyrajziSzam helyrajziSzam : helyrajziSzamList) {
            for (Erdekeltseg erdekeltseg : helyrajziSzam.getErdekeltsegList()) {
                erdekeltseg.setMuvelesiAgList(new ArrayList<MuvelesiAg>());
                for (MuvelesiAg muvelesiAg : helyrajziSzam.getMuvelesiAgList()) {
                    MuvelesiAg muvAg = new MuvelesiAg();
                    muvAg.setMegnevezes(muvelesiAg.getMegnevezes());
                    muvAg.setKivettMegnevezes(muvelesiAg.getKivettMegnevezes());
                    muvAg.setTerulet(Math.round(muvelesiAg.getTerulet() * erdekeltseg.getHanyad() * 10000) / 10000.0);
                    erdekeltseg.getMuvelesiAgList().add(muvAg);
                }
            }
        }

        ///// Writing
        Sheet sheet = wb.createSheet("Számított");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Helyrajzi szám");
        header.createCell(1).setCellValue("Művelési Ág");
        header.createCell(2).setCellValue("Kivett Megnevezés");
        header.createCell(3).setCellValue("Érdekeltség Típus");
        header.createCell(4).setCellValue("Érdekelt");
        header.createCell(5).setCellValue("Tulajdoni Hányad");
        header.createCell(6).setCellValue("Terület");
        int row_index = 1;
        for (HelyrajziSzam helyrajziSzam : helyrajziSzamList) {

            for (Erdekeltseg erdekeltseg : helyrajziSzam.getErdekeltsegList()) {

                for (MuvelesiAg muvelesiAg : erdekeltseg.getMuvelesiAgList()) {
                    Row row = sheet.createRow(row_index);
                    row.createCell(0).setCellValue(helyrajziSzam.getHrsz());
                    row.createCell(1).setCellValue(muvelesiAg.getMegnevezes());
                    row.createCell(2).setCellValue(muvelesiAg.getKivettMegnevezes());
                    row.createCell(3).setCellValue(erdekeltseg.getTipus());
                    row.createCell(4).setCellValue(erdekeltseg.getErdekelt());
                    row.createCell(5).setCellValue(erdekeltseg.getHanyadStr());
                    row.createCell(6).setCellValue(muvelesiAg.getTerulet());
                    row_index++;
                }

            }

        }
        sheet.createRow(row_index).createCell(6).setCellFormula("SUM(G2:G" + (row_index) + ")");

        String newFilename = filename.split("\\.")[0] + "_calc.xlsx";
        FileOutputStream fos = new FileOutputStream(filename);
        wb.write(fos);
        fos.close();

        ////

//        double ter1 = 0;
//        double ter2 = 0;
//        for (HelyrajziSzam helyrajziSzam : helyrajziSzamList) {
//            for (MuvelesiAg muvelesiAg : helyrajziSzam.getMuvelesiAgList()) {
//                ter1 += muvelesiAg.getTerulet();
//            }
//
//            for (Erdekeltseg erdekeltseg : helyrajziSzam.getErdekeltsegList()) {
//                for (MuvelesiAg muvelesiAg : erdekeltseg.getMuvelesiAgList()) {
//                    ter2 += muvelesiAg.getTerulet();
//                }
//            }
//        }
//
//        System.out.println(ter1 + " : " + ter2 + ", eltérés: " + (ter1 - ter2));
//        ArrayList<double[]> sums = new ArrayList<>();
//
//        for (HelyrajziSzam helyrajziSzam : helyrajziSzamList) {
//            sums.add(checkAndRound(helyrajziSzam));
//        }
//
//        ter1 = 0;
//        ter2 = 0;
//        for (double[] sum : sums) {
//            ter1 += sum[0];
//            ter2 += sum[1];
//        }
//
//        System.out.println("Összes terület: " + ter1 + ", számított terület: " + ter2);
//        System.out.println(" -> Eltérés: " + (ter1 - ter2));

    }

    private static double[] checkAndRound(HelyrajziSzam helyrajziSzam) {
        double[] terulet = new double[helyrajziSzam.getMuvelesiAgList().size()];
        double[] terulet2 = new double[helyrajziSzam.getMuvelesiAgList().size()];
        for (int i = 0; i < helyrajziSzam.getMuvelesiAgList().size(); i++) {
            MuvelesiAg muvelesiAg = helyrajziSzam.getMuvelesiAgList().get(i);
            terulet[i] += muvelesiAg.getTerulet();
            terulet2 = new double[helyrajziSzam.getMuvelesiAgList().size()];
            for (Erdekeltseg erdekeltseg : helyrajziSzam.getErdekeltsegList()) {
                for (int j = 0; j < erdekeltseg.getMuvelesiAgList().size(); j++) {
                    MuvelesiAg muvelesiAg1 = erdekeltseg.getMuvelesiAgList().get(j);
                    terulet2[j] += muvelesiAg1.getTerulet();
                }
            }

        }
        double[] sum = new double[2];
        for (int i = 0; i < helyrajziSzam.getMuvelesiAgList().size(); i++) {
            MuvelesiAg muvelesiAg = helyrajziSzam.getMuvelesiAgList().get(i);
            System.out.println(helyrajziSzam.getHrsz() + ", " + muvelesiAg.getMegnevezes() + " terület: " + terulet[i] + " számított terület: " + terulet2[i]);
            sum[0] += terulet[i];
            sum[1] += terulet2[i];

        }
        System.out.println(" -> Eltérés: " + (sum[0] - sum[1]));
        return sum;
    }

    private static void print(List<HelyrajziSzam> helyrajziSzamList) {
        for (HelyrajziSzam helyrajziSzam : helyrajziSzamList) {
            System.out.println(helyrajziSzam.getHrsz());
            for (MuvelesiAg muvelesiAg : helyrajziSzam.getMuvelesiAgList()) {
                System.out.println("\t" + muvelesiAg.getMegnevezes() + "\t" + muvelesiAg.getTerulet());
            }
            for (Erdekeltseg erdekeltseg : helyrajziSzam.getErdekeltsegList()) {
                System.out.println("\t" + erdekeltseg.getErdekelt().substring(15));
                double ter = 0;
                for (MuvelesiAg muvelesiAg : erdekeltseg.getMuvelesiAgList()) {
                    ter += muvelesiAg.getTerulet();
                    System.out.println("\t\t" + muvelesiAg.getMegnevezes() + "\t\t" + muvelesiAg.getTerulet());
                }
                System.out.println("Terület: \t" + ter);
            }
        }
    }

}
