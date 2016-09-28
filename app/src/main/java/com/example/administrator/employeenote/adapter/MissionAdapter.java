package com.example.administrator.employeenote.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.employeenote.R;
import com.example.administrator.employeenote.entity.VoiceData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by GE11522 on 2016/9/28.
 */

public class MissionAdapter extends BaseAdapter {

    private static final String TAG = "retrofit";


    public List<VoiceData> data;
    private LayoutInflater mInflater;
    private Context context;


    public MissionAdapter(Context context, List<VoiceData> data) {
        this.context = context;
        this.data = data;
        mInflater = LayoutInflater.from(context);

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MissionAdapter.ViewHold hold = null;
        if (convertView == null) {
            hold = new MissionAdapter.ViewHold();
            convertView = mInflater.inflate(R.layout.list_mission, null);
            hold.yearView = (TextView) convertView.findViewById(R.id.year);
            hold.dateView = (TextView) convertView.findViewById(R.id.date);
            hold.playView = (Button) convertView.findViewById(R.id.btn_play);
            hold.delView = (Button) convertView.findViewById(R.id.btn_delete);
            hold.traceView = (Button) convertView.findViewById(R.id.btn_review);
            convertView.setTag(hold);
        } else {
            hold = (MissionAdapter.ViewHold) convertView.getTag();
        }
        hold.yearView.setText(data.get(position).getVtime().toString().substring(0, 4) + "年");
        hold.dateView.setText(data.get(position).getVtime().toString().substring(5, 16).replace('-', '月').replace(' ', '日'));
        hold.playView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initVoice("http://60.205.178.163:8080/gesac/" + data.get(position).getVsrc(),
                        data.get(position).getVsrc().replaceAll("voice/", ""));

            }
        });
//        hold.jobView.setText(data.get(position).getEjob());
        //TODO 获取实时位置点
        return convertView;
    }

    private interface getVoiceIF {
        @GET
        Call<ResponseBody> getVoice(@Url String fileUrl);
    }

    private void initVoice(String url, final String filename) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://60.205.178.163:8080/gesac/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        getVoiceIF downloadService = retrofit.create(getVoiceIF.class);

        Call<ResponseBody> call = downloadService.getVoice(url);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "server contacted and has file");
                    boolean writtenToDisk = writeResponseBodyToDisk(response.body(), filename);
                    Log.d(TAG, "file download was a success? " + writtenToDisk);
                } else {
                    Log.d(TAG, "server contact failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "error");
            }
        });
    }

    private boolean writeResponseBodyToDisk(ResponseBody body, String filename) {
        try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(context.getCacheDir() + File.separator + filename);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }


    class ViewHold {
        //public ImageView image;
        public TextView yearView, dateView;
        public Button playView, delView, traceView;
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
