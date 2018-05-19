package vn.digital.signage.android.utils.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.view.Gravity;

import vn.digital.signage.android.R;

public final class AsyncTaskManager implements IProgressTracker, OnCancelListener {

    private final OnTaskCompleteListener mTaskCompleteListener;
    private ProgressDialog mProgressDialog;
    private MediaDownloadTask mAsyncMediaDownloadTask;

    public AsyncTaskManager(Context context, OnTaskCompleteListener taskCompleteListener) {

        final String pdMsg = context.getString(R.string.progress_dialog_title_downloading);

        // Save reference to complete listener (activity)
        mTaskCompleteListener = taskCompleteListener;

        // Setup progress dialog
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.getWindow().setGravity(Gravity.BOTTOM | Gravity.LEFT);
        // mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setMessage(pdMsg);
        // Setup progress dialog
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setOnCancelListener(this);
    }

    public void setupTask(MediaDownloadTask asyncMediaDownloadTask) {
        // Keep task
        mAsyncMediaDownloadTask = asyncMediaDownloadTask;
        // Wire task to tracker (this)
        mAsyncMediaDownloadTask.setProgressTracker(this);
        // Start task
        mAsyncMediaDownloadTask.execute();
    }

    public void showDialog(boolean isShow) {
        if (mProgressDialog != null) {
            if (isShow) {
                mProgressDialog.show();
            } else {
                mProgressDialog.hide();
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }
        }
    }

    @Override
    public void onProgress(String message) {
        // Show current message in progress dialog
        mProgressDialog.setMessage(message);
    }

    @Override
    public void onComplete() {
        // Close progress dialog
        if (mProgressDialog != null) {
            mProgressDialog.hide();
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
        // Notify activity about completion
        mTaskCompleteListener.onTaskComplete(mAsyncMediaDownloadTask);
        // Reset task
        mAsyncMediaDownloadTask = null;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        // Cancel task
        mAsyncMediaDownloadTask.cancel(true);
        // Notify activity about completion
        mTaskCompleteListener.onTaskComplete(mAsyncMediaDownloadTask);
        // Reset task
        mAsyncMediaDownloadTask = null;
    }

    public Object retainTask() {
        // Detach task from tracker (this) before retain
        if (mAsyncMediaDownloadTask != null) {
            mAsyncMediaDownloadTask.setProgressTracker(null);
        }
        // Retain task
        return mAsyncMediaDownloadTask;
    }

    public void handleRetainedTask(Object instance) {
        // Restore retained task and attach it to tracker (this)
        if (instance instanceof MediaDownloadTask) {
            mAsyncMediaDownloadTask = (MediaDownloadTask) instance;
            mAsyncMediaDownloadTask.setProgressTracker(this);
        }
    }

    public boolean isWorking() {
        // Track current status
        return mAsyncMediaDownloadTask != null;
    }
}