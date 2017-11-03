package ru.biophotonics.msu.pdfgenerator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
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
    private Context mContext;


    public PdfGenerator(String fileName,Context context) throws IOException {
        createFilesFolder();
        setFilePath(fileName);
        setContext(context);
        setPaint();
    }

    public PdfGenerator(String fileName, String message, Context context) throws IOException {
        createFilesFolder();
        setFilePath(fileName);
        setMessage(message);
        setContext(context);
        setPaint();
    }
    /**
     * createFilesFolder with name
     * @FOLDER_NAME in Documents directory
     */
    private void createFilesFolder() {

        boolean creationSucceed = false;
        File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
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

    private void setContext(Context context){
        mContext = context;
    }

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
     *
     * @param bitmap - that you want to scale
     * @param dst - coordinates of rectanlge on canvas that you want where you wanna scale
     * @param stf - scaling options, see android documentation
     * @return matrix with translation and
     */
    private Matrix getScalingMatrix(Bitmap bitmap, RectF dst,Matrix.ScaleToFit stf){
        RectF src = new RectF(0,0,bitmap.getWidth(),bitmap.getHeight());

        Matrix matrix = new Matrix();

        boolean hasTranslated = matrix.setRectToRect(src,dst,stf);

        if(hasTranslated){
            Log.d(TAG,"getScalingMatrix:: scaled successful");
        } else{
            Log.e(TAG,"getScalingMatrix:: scaling failed!");
        }

        return matrix;
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


        //drawing bitmap from drawable
        Bitmap logo = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.logo);

        Matrix logoMatrix = getScalingMatrix(logo,new RectF(0,0,50,50), Matrix.ScaleToFit.CENTER);
        pageCanvas.drawBitmap(logo,logoMatrix,null);

        pageCanvas.drawLine(50,50,50,150,mPaint);

        //drawing Rectangle using current paint
        mPaint.setAlpha(100);
        mPaint.setColor(Color.GREEN);
        pageCanvas.drawRect(new Rect(60,60,120,80),mPaint);

        mPaint.setColor(Color.RED);
        mPaint.setAlpha(255);

        mPaint.setTextSize(5);
        pageCanvas.drawText("anotherTextExample",70,70,mPaint);

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
