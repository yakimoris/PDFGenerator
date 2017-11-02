package ru.biophotonics.msu.pdfgenerator;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by boris on 03.11.17.
 */

public class PdfGenerator {

    private static final String TAG = "pdfGenerator";
    private static final String FOLDER_NAME = "pdfGenerator";
    private static final int PAGE_WIDTH = 200;
    private static final int PAGE_HEIGHT = 200;
    private static final int NUMBER_OF_PAGES = 1;

    private static final float MessageCoordinates_X = 100;
    private static final float MessageCoordinates_Y = 100;

    private File mFolderPath;
    private File mFilePath;
    private String mMessage;
    private Paint mPaint;


    public PdfGenerator(String fileName) throws IOException {
        createFilesFolder();
        setFilePath(fileName);

        setPaint();
    }

    public PdfGenerator(String fileName, String message) throws IOException {
        createFilesFolder();
        setFilePath(fileName);
        setMessage(message);
        setPaint();
    }
    /**
     * createFilesFolder with name
     * @FOLDER_NAME in Documents directory
     */
    private void createFilesFolder() {

        boolean creationSucceed = false;
        File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        mFolderPath = new File(folder, FOLDER_NAME);

        if(!mFolderPath.exists()) {
            creationSucceed = mFolderPath.mkdirs();
        }

        creationSucceed = mFolderPath.exists() || creationSucceed;

        if(!creationSucceed){
            Log.e(TAG,"Creation failed");
        } else {
            Log.d(TAG,"files folder created successfully");
        }
    }

    /**
     * String
     * @param filePrepend with prepend (it means without extension ".pdf")
     * @throws IOException is thrown if error occurs during File creation
     */
    private void setFilePath(String filePrepend) throws IOException {
        File filePath = new File(mFolderPath,filePrepend + ".pdf");
        mFilePath = filePath;
        Log.d(TAG,"setFilePath:: mFilePath:: "+mFilePath.getAbsolutePath());
    }

    /**
     *
     * @return outputStream where to write file
     * @throws IOException if it is impossible to create FileStream
     */
    private FileOutputStream getOutputStream() throws IOException{
        FileOutputStream output;
        output = new FileOutputStream(mFilePath.getAbsolutePath());

        return output;
    }

    /**
     * set paint to draw on canvas
     */
    private void setPaint() {
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
    }

    /**
     * set
     * @param message that will be written in PDF document
     */
    private void setMessage(String message){
        mMessage = message;
    }


    /**
     * this method generates PDF file with set previously message and
     * @return true if file was generated successfully
     */
    public boolean generatePDF(){
        // create a new document
        PdfDocument document = new PdfDocument();

        // create a page description
        PdfDocument.PageInfo pageInfo
                = new PdfDocument.PageInfo.Builder(PAGE_WIDTH,PAGE_HEIGHT, NUMBER_OF_PAGES).create();

        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);

        //getting pageCanvas - we're going to draw here
        Canvas pageCanvas = page.getCanvas();

        pageCanvas.drawText(mMessage,MessageCoordinates_X,MessageCoordinates_Y,mPaint);
        // finish the page
        document.finishPage(page);

        try {
            document.writeTo(getOutputStream());

        } catch (IOException e) {
            Log.e(TAG,"Error in creating output stream");
            e.printStackTrace();
            return false;
        }

        // close the document
        document.close();

        return true;
    }
}
