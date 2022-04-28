package com.example.createpdfdocument;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private Button create_and_open_PDF;

    // declaring width and height
    // for our PDF file.
    int pageHeight = 1754;
    int pagewidth = 1240;

    // creating a bitmap variable
    // for storing our images
    Bitmap bmp, scaledbmp;
    //--for design
    Bitmap background, socialMedia, logo, qrCode;

    // constant code for runtime permissions
    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // initializing our variables.
        this.create_and_open_PDF = (Button) findViewById(R.id.create_and_open_PDF);

        //--for design
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.background_design);
        background = Bitmap.createScaledBitmap(bmp, pagewidth, pageHeight, false);

        socialMedia = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.social_media), 158, 30, false);
        logo = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.logo), 178, 178, false);
        qrCode = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.qr_code), 85, 85, false);

        // below code is used for
        // checking our permissions.
        if (checkPermission()) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            requestPermission();
        }

        create_and_open_PDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calling method to
                // generate our PDF file.
                //generatePDF();
                generatePDFInvoice();
                // open our PDF file click inside of fonction 'generatePDF();'
            }
        });
    }

    private void generatePDFInvoice() {
        // create a new document
        PdfDocument document = new PdfDocument();

        // create a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();

        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);

        // draw something on the page Using 'getContentView()'
        design(page);

        // finish the page
        document.finishPage(page);
// . . .
        // add more pages
// . . .
        // write the document content
        File file = new File(Environment.getExternalStorageDirectory(), "PDFTest.pdf");
        try {
            document.writeTo(new FileOutputStream(file));
            Toast.makeText(MainActivity.this, "PDF file generated successfully.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // close the document
        document.close();

        // open our PDF file
        openPdf(Uri.fromFile(file));
    }

    private void design(PdfDocument.Page page) {
        Paint paint = new Paint();
        Paint title = new Paint();

        Canvas canvas = page.getCanvas();

        canvas.drawBitmap(background, 0, 0, paint);
        canvas.drawBitmap(logo, 72, 62, paint);
        canvas.drawBitmap(socialMedia, 76, 1654, paint);
        canvas.drawBitmap(qrCode, 1016, 1588, paint);

        Typeface nunito_bold = Typeface.create(Typeface.createFromAsset(getAssets(), "nunito_bold.ttf"), Typeface.NORMAL);
        Typeface nunito_regular = Typeface.create(Typeface.createFromAsset(getAssets(), "nunito_regular.ttf"), Typeface.NORMAL);

        designsText(canvas, title, 95, R.color.blue, 696, 164, nunito_bold, "Facture");

        designsText(canvas, title, 28, R.color.blue, 88, 304, nunito_bold, "Facture");
        designsText(canvas, title, 28, R.color.blue, 192, 304, nunito_regular, "pour");
        designsText(canvas, title, 36, R.color.blue, 88, 366, nunito_bold, "Idir Belmokhtar");
        designsText(canvas, title, 24, R.color.blue, 88, 422, nunito_bold, "belmokhtaridir@gmail.com");
        designsText(canvas, title, 24, R.color.blue, 88, 456, nunito_bold, "+213 659 23 92 69");
        designsText(canvas, title, 24, R.color.blue, 88, 490, nunito_bold, "Algérie ");

        designsText(canvas, title, 25, R.color.blue, 720, 504, nunito_bold, "Facture #");
        designsText(canvas, title, 24, R.color.blue, 880, 504, nunito_bold, "00001");

        designsText(canvas, title, 25, R.color.blue, 720, 558, nunito_bold, "Date");
        designsText(canvas, title, 24, R.color.blue, 880, 558, nunito_bold, "11 / 04 / 2022");

        designsText(canvas, title, 28, R.color.white, 162, 663, nunito_bold, "N°");
        designsText(canvas, title, 28, R.color.white, 304, 663, nunito_bold, "Description de l'article");
        designsText(canvas, title, 28, R.color.white, 702, 663, nunito_bold, "Prix");
        designsText(canvas, title, 28, R.color.white, 890, 663, nunito_bold, "Qnt.");
        designsText(canvas, title, 28, R.color.white, 1012, 663, nunito_bold, "Total");

        designsText(canvas, title, 24, R.color.blue, 162, 796, nunito_bold, "1");
        designsText(canvas, title, 17, R.color.blue, 304, 796, nunito_bold, "Expédition PUBG Mobile Globale ID");
        designsText(canvas, title, 24, R.color.blue, 702, 796, nunito_bold, "1 000 DA");
        designsText(canvas, title, 24, R.color.blue, 890, 796, nunito_bold, "1");
        designsText(canvas, title, 24, R.color.blue, 998, 794, nunito_bold, "1 000 DA");

        designsText(canvas, title, 24, R.color.blue, 162, 867, nunito_bold, "2");
        designsText(canvas, title, 17, R.color.blue, 304, 867, nunito_bold, "Expédition PUBG Mobile Globale ID");
        designsText(canvas, title, 24, R.color.blue, 702, 867, nunito_bold, "2 000 DA");
        designsText(canvas, title, 24, R.color.blue, 890, 867, nunito_bold, "2");
        designsText(canvas, title, 24, R.color.blue, 998, 865, nunito_bold, "4 000 DA");

        designsText(canvas, title, 24, R.color.blue, 162, 940, nunito_bold, "3");
        designsText(canvas, title, 17, R.color.blue, 304, 940, nunito_bold, "Expédition PUBG Mobile Globale ID");
        designsText(canvas, title, 24, R.color.blue, 702, 940, nunito_bold, "2 250 DA");
        designsText(canvas, title, 24, R.color.blue, 890, 940, nunito_bold, "2");
        designsText(canvas, title, 24, R.color.blue, 998, 938, nunito_bold, "4 500 DA");

        designsText(canvas, title, 24, R.color.blue, 162, 1013, nunito_bold, "4");
        designsText(canvas, title, 17, R.color.blue, 304, 1013, nunito_bold, "Expédition PUBG Mobile Globale ID");
        designsText(canvas, title, 24, R.color.blue, 702, 1013, nunito_bold, "2 000 DA");
        designsText(canvas, title, 24, R.color.blue, 890, 1013, nunito_bold, "5");
        designsText(canvas, title, 24, R.color.blue, 998, 1011, nunito_bold, "10 000 DA");

        designsText(canvas, title, 24, R.color.blue, 162, 1087, nunito_bold, "5");
        designsText(canvas, title, 17, R.color.blue, 304, 1087, nunito_bold, "Expédition PUBG Mobile Globale ID");
        designsText(canvas, title, 24, R.color.blue, 702, 1087, nunito_bold, "2 000 DA");
        designsText(canvas, title, 24, R.color.blue, 890, 1087, nunito_bold, "5");
        designsText(canvas, title, 24, R.color.blue, 998, 1085, nunito_bold, "10 000 DA");

        designsText(canvas, title, 28, R.color.blue, 88, 1238, nunito_bold, "Paiement");
        designsText(canvas, title, 28, R.color.blue, 218, 1238, nunito_regular, "info:");

        designsText(canvas, title, 24, R.color.blue, 88, 1294, nunito_bold, "Compte #");
        designsText(canvas, title, 24, R.color.blue, 292, 1294, nunito_regular, "3256 2295 2346");

        designsText(canvas, title, 24, R.color.blue, 88, 1350, nunito_bold, "A/C Name");
        designsText(canvas, title, 24, R.color.blue, 292, 1350, nunito_regular, "idir7_");

        designsText(canvas, title, 24, R.color.blue, 88, 1406, nunito_bold, "Détails");
        designsText(canvas, title, 24, R.color.blue, 292, 1406, nunito_regular, "PUBG Mobile Globale ID");

        designsText(canvas, title, 28, R.color.blue, 726, 1238, nunito_bold, "Sous Total:");
        designsText(canvas, title, 28, R.color.blue, 956, 1238, nunito_bold, "1 000.00 DA");

        designsText(canvas, title, 28, R.color.blue, 726, 1314, nunito_bold, "Taxe:");
        designsText(canvas, title, 28, R.color.blue, 956, 1314, nunito_bold, "0.00%");

        designsText(canvas, title, 34, R.color.blue, 726, 1408, nunito_bold, "Total:");
        designsText(canvas, title, 32, R.color.blue, 946, 1408, nunito_bold, "1 000.00 DA");

        designsText(canvas, title, 28, R.color.blue, 88, 1506, nunito_bold, "Termes    Conditions:");
        designsText(canvas, title, 28, R.color.blue, 190, 1506, nunito_regular, "&");
        designsText(canvas, title, 24, R.color.blue, 88, 1554, nunito_regular, "Les offres vendues");
        designsText(canvas, title, 24, R.color.blue, 88, 1582, nunito_regular, "ne sont ni retournables");
        designsText(canvas, title, 24, R.color.blue, 88, 1610, nunito_regular, "ni échangeables.");

        designsText(canvas, title, 22, R.color.blue, 726, 1606, nunito_bold, "SCANNEZ LE QRCODE:");

        designsText(canvas, title, 16, R.color.blue, 264, 1682, nunito_regular, "myurls.co/gameshop_services_dz");

    }

    private void designsText(Canvas canvas, Paint title, int size, int colour, int x, int y, Typeface typeface, String text) {

        title.setColor(ContextCompat.getColor(this, colour));
        title.setTypeface(typeface);
        title.setTextSize(size);

        canvas.drawText(text, x, y, title);
    }

    private void generatePDF() {

        // creating an object variable
        // for our PDF document.
        PdfDocument pdfDocument = new PdfDocument();

        // two variables for paint "paint" is used
        // for drawing shapes and we will use "title"
        // for adding text in our PDF file.
        Paint paint = new Paint();
        Paint title = new Paint();

        // we are adding page info to our PDF file
        // in which we will be passing our pageWidth,
        // pageHeight and number of pages and after that
        // we are calling it to create our PDF.
        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();

        // below line is used for setting
        // start page for our PDF file.
        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);

        // creating a variable for canvas
        // from our page of PDF.
        Canvas canvas = myPage.getCanvas();

        // below line is used to draw our image on our PDF file.
        // the first parameter of our drawbitmap method is
        // our bitmap
        // second parameter is position from left
        // third parameter is position from top and last
        // one is our variable for paint.
        canvas.drawBitmap(scaledbmp, 56, 40, paint);

        // below line is used for adding typeface for
        // our text which we will be adding in our PDF file.
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

        // below line is used for setting text size
        // which we will be displaying in our PDF file.
        title.setTextSize(15);

        // below line is sued for setting color
        // of our text inside our PDF file.
        title.setColor(ContextCompat.getColor(this, R.color.purple_200));

        // below line is used to draw text in our PDF file.
        // the first parameter is our text, second parameter
        // is position from start, third parameter is position from top
        // and then we are passing our variable of paint which is title.
        canvas.drawText("A portal for IT professionals.", 209, 100, title);
        canvas.drawText("Geeks for Geeks", 209, 80, title);

        // similarly we are creating another text and in this
        // we are aligning this text to center of our PDF file.
        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        title.setColor(ContextCompat.getColor(this, R.color.purple_200));
        title.setTextSize(15);

        // below line is used for setting
        // our text to center of PDF.
        title.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("This is sample document which we have created.", 396, 560, title);

        // after adding all attributes to our
        // PDF file we will be finishing our page.
        pdfDocument.finishPage(myPage);

        // below line is used to set the name of
        // our PDF file and its path.
        File file = new File(Environment.getExternalStorageDirectory(), "PDFTest.pdf");

        try {
            // after creating a file name we will
            // write our PDF file to that location.
            pdfDocument.writeTo(new FileOutputStream(file));

            // below line is to print toast message
            // on completion of PDF generation.
            Toast.makeText(MainActivity.this, "PDF file generated successfully.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            // below line is used
            // to handle error
            e.printStackTrace();
        }
        // after storing our pdf to that
        // location we are closing our PDF file.
        pdfDocument.close();

        // open our PDF file
        openPdf(Uri.fromFile(file));
    }

    private void openPdf(Uri uri) {
        Intent intent = new Intent(MainActivity.this, PdfViewerActivity.class);

        intent.putExtra("uri_string_file", uri.toString());

        startActivities(new Intent[]{intent});
    }

    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {

                // after requesting permissions we are showing
                // users a toast message of permission granted.
                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denined.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }
}