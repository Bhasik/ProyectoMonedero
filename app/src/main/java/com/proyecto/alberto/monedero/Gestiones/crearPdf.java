package com.proyecto.alberto.monedero.Gestiones;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.proyecto.alberto.monedero.R;
import com.proyecto.alberto.monedero.Tablas.Movimiento_PDF;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class crearPdf {

    /**
     * Creates a PDF document.
     *
     * @throws DocumentException
     * @throws java.io.IOException
     */
    //Ruta del archivo, esto es dentro del proyecto Netbeans
    public static String archivo = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/informe.pdf";
    private Activity context;
    private ArrayList<Movimiento_PDF> datos;
    private String fechaInicio;
    private String fechaFin;
    private String kmsInciales;
    private String kmsFinales;

    public crearPdf(ArrayList<Movimiento_PDF> datos, String fechaInicio, String fechaFin, String kmsInciales, String kmsFinales, Activity context) {

        this.datos = datos;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.kmsInciales = kmsInciales;
        this.kmsFinales = kmsFinales;
        this.context = context;
    }


    public void createPdf() throws DocumentException {

    /*Declaramos documento como un objeto Document
      Asignamos el tamaño de hoja y los margenes */
        Document documento = new Document(PageSize.LETTER.rotate(), 80, 80, 75, 75);

        //writer es declarado como el método utilizado para escribir en el archivo
        PdfWriter writer = null;

        try {
            //Obtenemos la instancia del archivo a utilizar
            writer = PdfWriter.getInstance(documento, new FileOutputStream(archivo));

        } catch (FileNotFoundException | DocumentException ex) {
            ex.getMessage();
        }

        int kmRecorrido = kmsRecorridos();

        //Agregamos un titulo al archivo
        documento.addTitle("Archivo pdf generado desde Java");

        //Agregamos el autor del archivo
        documento.addAuthor("Alberto");

        //Abrimos el documento para edición
        documento.open();

        //Declaramos un texto como Paragraph
        //Le podemos dar formado como alineación, tamaño y color a la fuente.
        Paragraph parrafo = new Paragraph();
        parrafo.setAlignment(Paragraph.ALIGN_CENTER);
        parrafo.setFont(FontFactory.getFont("Sans", 20, Font.BOLD, BaseColor.BLUE));
        parrafo.add("Informe Taxi");


        Paragraph parrafo2 = new Paragraph();
        parrafo2.setFont(FontFactory.getFont("Sans", 12, Font.BOLD, BaseColor.BLACK));
        parrafo2.setSpacingAfter(5f);
        parrafo2.add(context.getString(R.string.fechaInicialPDF) + fechaInicio + "\t \t \t \t \t \t ");
        parrafo2.add(context.getString(R.string.kmsInicialesPDF) + kmsInciales + "\t \t \t \t \t \t ");
        parrafo2.add(context.getString(R.string.kmsRecorridosPDF) + kmRecorrido);
        parrafo2.setAlignment(Element.ALIGN_LEFT);


        long numeroDias = diferenciaEnDias2();

        Paragraph parrafo3 = new Paragraph();
        parrafo3.setFont(FontFactory.getFont("Sans", 12, Font.BOLD, BaseColor.BLACK));
        parrafo3.add(context.getString(R.string.fechaFinalPDF) + fechaFin + "\t \t \t \t \t \t ");
        parrafo3.add(context.getString(R.string.kmsFinalesPDF) + kmsFinales + "\t \t \t \t \t \t ");
        parrafo3.add(context.getString(R.string.numeroDiasPDF) + numeroDias);

        parrafo3.setAlignment(Element.ALIGN_LEFT);


        Paragraph parrafo4 = new Paragraph();
        parrafo4.setFont(FontFactory.getFont("Sans", 12, Font.BOLD, BaseColor.BLACK));
        parrafo4.add(context.getString(R.string.totalPeriodoPDF) + calcularTotalPeriodo() + "\t \t \t \t \t \t \t \t ");
        parrafo4.add("\t \t \t \t \t \t \t \t " + context.getString(R.string.totalDiaPDF) + calcularTotalPorDia());
        parrafo4.add("\t \t \t \t \t \t \t \t " + context.getString(R.string.totalKmPDF) + calcularTotalPorKm());


        try {

            //Agregamos el texto al documento
            documento.add(parrafo);
            documento.add(parrafo2);
            documento.add(parrafo3);

            //Agregamos un salto de linea
            documento.add(new Paragraph(" "));

            //Agregamos la tabla al documento haciendo
            //la llamada al método tabla()
            documento.add(tabla());

            //Agregamos el texto despues de la tabla
            documento.add(parrafo4);

        } catch (DocumentException ex) {
            ex.getMessage();
        }

        documento.close(); //Cerramos el documento
        writer.close(); //Cerramos writer
    }

    //Método para crear la tabla
    public PdfPTable tabla() throws DocumentException {
        //Instanciamos una tabla de 3 columnas
        PdfPTable tabla = new PdfPTable(10);

        // ASIGNAS LAS MEDIDAS A LA TABLA (ANCHO)
        tabla.setWidthPercentage(118f);

        //Declaramos un objeto para manejar las celdas
        PdfPCell celda;

        BaseColor color = new BaseColor(255, 255, 204);


        //Agregamos una frase a la celda
        celda = new PdfPCell(new Phrase(context.getString(R.string.tipoPDF)));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        //Agregamos la celda a la tabla
        celda.setBorder(Rectangle.RIGHT);
        celda.setBorderColor(new BaseColor(210, 210, 210));

        celda.setBackgroundColor(color);
        tabla.addCell(celda);

        celda = new PdfPCell(new Phrase(context.getString(R.string.conceptoPDF)));
        celda.setColspan(2);
        celda.setBorder(Rectangle.RIGHT);
        celda.setBorderColor(new BaseColor(210, 210, 210));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_CENTER);
        celda.setBackgroundColor(color);
        tabla.addCell(celda);

        celda = new PdfPCell(new Phrase(context.getString(R.string.importeFijoPDF)));
        celda.setBorder(Rectangle.RIGHT);
        celda.setBorderColor(new BaseColor(210, 210, 210));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_CENTER);
        celda.setBackgroundColor(color);
        tabla.addCell(celda);

        celda = new PdfPCell(new Phrase(context.getString(R.string.diasPeriodoPDF)));
        celda.setBorder(Rectangle.RIGHT);
        celda.setBorderColor(new BaseColor(210, 210, 210));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_CENTER);
        celda.setBackgroundColor(color);
        tabla.addCell(celda);

        celda = new PdfPCell(new Phrase(context.getString(R.string.importeDiaPDF)));
        celda.setBorder(Rectangle.RIGHT);
        celda.setBorderColor(new BaseColor(210, 210, 210));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_CENTER);
        celda.setBackgroundColor(color);
        tabla.addCell(celda);

        celda = new PdfPCell(new Phrase(context.getString(R.string.importePeriodoPDF)));
        celda.setBorder(Rectangle.RIGHT);
        celda.setBorderColor(new BaseColor(210, 210, 210));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_CENTER);
        celda.setBackgroundColor(color);
        tabla.addCell(celda);

        celda = new PdfPCell(new Phrase(context.getString(R.string.numeroOpsPDF)));
        celda.setBorder(Rectangle.RIGHT);
        celda.setBorderColor(new BaseColor(210, 210, 210));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_CENTER);
        celda.setBackgroundColor(color);
        tabla.addCell(celda);

        celda = new PdfPCell(new Phrase(context.getString(R.string.porDiaPDF)));
        celda.setBorder(0);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_CENTER);
        celda.setBackgroundColor(color);
        tabla.addCell(celda);

        celda = new PdfPCell(new Phrase(context.getString(R.string.porKmPDF)));
        celda.setBorder(0);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_CENTER);
        celda.setBackgroundColor(color);
        tabla.addCell(celda);
        String tipo;

        int fila = 1;


        for (Movimiento_PDF dato : datos) {

            if (fila % 2 != 0) {

                color = new BaseColor(255, 255, 0);


            } else {

                color = new BaseColor(255, 255, 204);

            }

            if (dato.getTipo() == 1) {

                tipo = "1";

            } else {

                tipo = "0";

            }

            celda = new PdfPCell(new Phrase(tipo));
            celda.setBackgroundColor(color);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(Rectangle.RIGHT);
            celda.setBorderColor(new BaseColor(210, 210, 210));
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase(dato.getNombre()));
            celda.setColspan(2);
            celda.setBackgroundColor(color);
            celda.setBorder(Rectangle.RIGHT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setPaddingLeft(7f);
            celda.setBorderColor(new BaseColor(210, 210, 210));
            tabla.addCell(celda);

            if (dato.getTipo() == 1) {

                celda = new PdfPCell(new Phrase(String.valueOf(dato.getImporte_fijo() + "€")));
                celda.setBackgroundColor(color);
                celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBorder(Rectangle.RIGHT);
                celda.setBorderColor(new BaseColor(210, 210, 210));
                celda.setPaddingRight(7f);
                tabla.addCell(celda);

                celda = new PdfPCell(new Phrase(String.valueOf(dato.getDias())));
                celda.setBackgroundColor(color);
                celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBorder(Rectangle.RIGHT);
                celda.setBorderColor(new BaseColor(210, 210, 210));
                celda.setPaddingRight(7f);
                tabla.addCell(celda);

                celda = new PdfPCell(new Phrase(String.valueOf(dato.getImporte_dia() + "€")));
                celda.setBackgroundColor(color);
                celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                celda.setVerticalAlignment(Element.ALIGN_CENTER);
                celda.setBorder(Rectangle.RIGHT);
                celda.setBorderColor(new BaseColor(210, 210, 210));
                celda.setPaddingRight(7f);
                tabla.addCell(celda);

            } else {

                celda = new PdfPCell(new Phrase(""));
                celda.setBackgroundColor(color);
                celda.setBorder(Rectangle.RIGHT);
                celda.setBorderColor(new BaseColor(210, 210, 210));
                celda.setPaddingRight(7f);
                tabla.addCell(celda);

                celda = new PdfPCell(new Phrase(""));
                celda.setBackgroundColor(color);
                celda.setBorder(Rectangle.RIGHT);
                celda.setBorderColor(new BaseColor(210, 210, 210));
                celda.setPaddingRight(7f);
                tabla.addCell(celda);

                celda = new PdfPCell(new Phrase(""));
                celda.setBackgroundColor(color);
                celda.setBorder(Rectangle.RIGHT);
                celda.setBorderColor(new BaseColor(210, 210, 210));
                celda.setPaddingRight(7f);
                tabla.addCell(celda);

            }

            celda = new PdfPCell(new Phrase(String.valueOf(dato.getImporte_periodo() + "€")));
            celda.setBackgroundColor(color);
            celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(Rectangle.RIGHT);
            celda.setBorderColor(new BaseColor(210, 210, 210));
            celda.setPaddingRight(7f);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase(String.valueOf(dato.getNum_operaciones())));
            celda.setBackgroundColor(color);
            celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setBorder(Rectangle.RIGHT);
            celda.setBorderColor(new BaseColor(210, 210, 210));
            celda.setPaddingRight(7f);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase(String.valueOf(dato.getImporte_dia() + "€")));
            celda.setBackgroundColor(color);
            celda.setBorder(0);
            celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setPaddingRight(7f);
            tabla.addCell(celda);

            Log.v("getKm", String.valueOf(dato.getPor_km()));
            celda = new PdfPCell(new Phrase(String.valueOf(dato.getPor_km() + "€")));
            celda.setBackgroundColor(color);
            celda.setBorder(0);
            celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
            celda.setVerticalAlignment(Element.ALIGN_CENTER);
            celda.setPaddingRight(7f);
            tabla.addCell(celda);


            fila++;
        }


        return tabla;
    }

    public double calcularTotalPeriodo() {

        double total = 0.0;

        for (Movimiento_PDF dato : datos) {

            total = total + dato.getImporte_periodo();

        }

        total = Math.round(total * Math.pow(10, 2)) / Math.pow(10, 2);

        return total;

    }

    public double calcularTotalPorDia() {

        double total = 0.0;

        for (Movimiento_PDF dato : datos) {

            total = total + dato.getImporte_dia();

        }

        total = Math.round(total * Math.pow(10, 2)) / Math.pow(10, 2);

        return total;

    }

    public double calcularTotalPorKm() {

        double total = 0.0;

        for (Movimiento_PDF dato : datos) {

            total = total + dato.getPor_km();

        }

        total = Math.round(total * Math.pow(10, 2)) / Math.pow(10, 2);

        return total;

    }

    public int kmsRecorridos() {

        int kmRecorridos;

        int kmFin = Integer.parseInt(kmsFinales);
        int kmIni = Integer.parseInt(kmsInciales);

        kmRecorridos = kmFin - kmIni;

        return kmRecorridos;

    }

    public long diferenciaEnDias2() {

        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd");
        Date fecha = null;
        Date fechaFinal = null;

        try {

            fecha = formatoDelTexto.parse(fechaInicio);
            fechaFinal = formatoDelTexto.parse(fechaFin);

        } catch (ParseException ex) {

            ex.printStackTrace();

        }

        long lantes = fecha.getTime();
        long lahora = fechaFinal.getTime();
        long dias = ((lahora - lantes) / (1000 * 60 * 60 * 24));

        return dias;

    }

}



