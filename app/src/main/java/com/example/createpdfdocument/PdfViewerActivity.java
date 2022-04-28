package com.example.createpdfdocument;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class PdfViewerActivity extends AppCompatActivity {

    PDFView pdfView;
    Uri Uri_file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        getSupportActionBar().hide();

        this.pdfView = (PDFView) findViewById(R.id.pdfView);

        if (getIntent().hasExtra("uri_string_file")){
            Uri_file = Uri.parse(getIntent().getStringExtra("uri_string_file"));
        }


        pdfView.fromUri(Uri_file).load();

        File file = new File(Uri_file.getPath());
        String i = String.valueOf(file.length()/1024);
        Toast.makeText(PdfViewerActivity.this, "Size: " + i + " Ko", Toast.LENGTH_SHORT).show();
    }
}