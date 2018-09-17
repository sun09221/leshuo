package com.example.feng.xgs.chatui.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.feng.xgs.R;
import com.example.feng.xgs.chatui.base.BaseFragment;
import com.example.feng.xgs.chatui.ui.activity.JGApplication;
import com.example.feng.xgs.chatui.ui.activity.MapPickerActivity;
import com.example.feng.xgs.config.callback.CallbackManager;
import com.example.feng.xgs.config.callback.CallbackType;
import com.example.feng.xgs.config.callback.IGlobalCallback;
import com.example.feng.xgs.config.key.CodeKeys;
import com.example.feng.xgs.config.key.ShareKeys;
import com.example.feng.xgs.utils.file.FileUtil;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;
import com.example.feng.xgs.utils.file.UriUtils;
import com.example.feng.xgs.utils.manager.LogUtils;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.LocationContent;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * 作者：Rance on 2016/12/13 16:01
 * 邮箱：rance935@163.com
 */
public class ChatFunctionFragment extends BaseFragment {
    private View rootView;
    private static final int CROP_PHOTO = 2;
    private static final int REQUEST_CODE_PICK_IMAGE = 3;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 6;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE2 = 7;
    private UserInfo mUserInfo;
    private File output;
    private Uri imageUri;
    String a;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_chat_function, container, false);
            ButterKnife.bind(this, rootView);
        }
        return rootView;
    }

    @OnClick({R.id.chat_function_photo, R.id.chat_function_photograph, R.id.chat_function_address})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chat_function_photograph:
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_CALL_PHONE2);

                } else {
                    takePhoto();
                }
                break;
            case R.id.chat_function_photo:
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_CALL_PHONE2);

                } else {
                    pickPhoto();
                }
                break;
            case R.id.chat_function_address:
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "请在应用管理中打开“位置”访问权限！", Toast.LENGTH_LONG).show();
                } else {
                    mUserInfo = JMessageClient.getMyInfo();
                    Intent   intent = new Intent(getContext(), MapPickerActivity.class);
                    intent.putExtra(JGApplication.TARGET_ID,mUserInfo.getUserName() );
                    intent.putExtra(JGApplication.TARGET_APP_KEY,mUserInfo.getAppKey() );
                    intent.putExtra("sendLocation", true);
                    startActivityForResult(intent,JGApplication.RESULT_CODE_SEND_LOCATION);
                }
                break;
        }
    }

    /**
     * 打开相机
     */
    private void takePhoto() {
        final String currentPhotoName = getPhotoName();
        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final File tempFile = new File(FileUtil.CAMERA_PHOTO_DIR, currentPhotoName);
        SharedPreferenceUtils.setCustomAppProfile(ShareKeys.CAMERA_FILE, tempFile.getPath());
        //适配7.0(7.0以后使用content Uri)
        Uri uriCamera = UriUtils.getUriForFile(getActivity(), tempFile);
//        CameraImageBean.getInstance().setPath(uriCamera);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriCamera);
        startActivityForResult(intent, CodeKeys.CAMERA_OPEN);
    }

    /**
     * 打开相册
     */
    private void pickPhoto() {
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        mFragment.startActivityForResult(intent, CodeKeys.PHOTO_OPEN);

        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
        startActivityForResult(intent, CodeKeys.PHOTO_OPEN);
    }

    private String getPhotoName() {
        return FileUtil.getFileNameByTime("FRIEND_CONTENT", "jpg");
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            switch (requestCode) {
                case CodeKeys.CAMERA_OPEN:
                    String cameraPath = SharedPreferenceUtils.getCustomAppProfile(ShareKeys.CAMERA_FILE);
                    LogUtils.d("onActivityResult: " + cameraPath);
                    callBack(cameraPath);
                    break;
                case CodeKeys.PHOTO_OPEN:
                    if (data != null) {
                        Uri photoUri = data.getData();
                        String photoPath ="";
                        if(photoUri.getScheme().equals("file")){
                            photoPath =photoUri.getPath();
                        }else {
                            photoPath = UriUtils.getPhotoPath(getActivity(), photoUri);
                        }
                        LogUtils.d("onActivityResult: " + photoPath);
                        callBack(photoPath);
                    }

                    break;

                case JGApplication.RESULT_CODE_SEND_LOCATION:
                //之前是在地图选择那边做的发送逻辑,这里是通过msgID拿到的message放到ui上.但是发现问题,message的status始终是send_going状态
                //因为那边发送的是自己创建的对象,这里通过id取出来的不是同一个对象.尽管内容都是一样的.所以为了保证发送的对象个ui上拿出来的
                //对象是同一个,就地图那边传过来数据,在这边进行创建message
                double latitude = data.getDoubleExtra("latitude", 0);
                double longitude = data.getDoubleExtra("longitude", 0);
                int mapview = data.getIntExtra("mapview", 0);
                String street = data.getStringExtra("street");
                String path = data.getStringExtra("path");
                LocationContent locationContent = new LocationContent(latitude,
                        longitude, mapview, street);
                locationContent.setStringExtra("path", path);
                    callBack2(latitude,longitude,mapview, street, path);
//                Message message = mConversation.createSendMessage(locationContent);
//                MessageSendingOptions options = new MessageSendingOptions();
//                options.setNeedReadReceipt(true);
 //               JMessageClient.sendMessage(message, options);
////                chatAdapter.addMsgFromReceiptToList(message);
////
////                int customMsgId = data.getIntExtra("customMsg", -1);
////                if (-1 != customMsgId) {
////                    Message customMsg = mConversation.getMessage(customMsgId);
////                    mChatAdapter.addMsgToList(customMsg);
////                }
////                mChatView.setToBottom();
////                break;
                default:
                    break;
            }
        }


    private void callBack(String path){
        @SuppressWarnings("unchecked") IGlobalCallback<String> callback = CallbackManager
                .getInstance()
                .getCallback(CallbackType.IM_IMAGE_MESSAGE);
        if (callback != null) {
            callback.executeCallback(path);
        }
    }
    private void callBack2(double latitude,double longitude,int mapview,String street,String path){
        @SuppressWarnings("unchecked") IGlobalCallback<String> callback = CallbackManager
                .getInstance()
                .getCallback(CallbackType.IM_LOCATION_MESSAGE);
        if (callback != null) {
            callback.executeCallback2(latitude,longitude,mapview, street, path);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto();
            } else {
                toastShow("请同意系统权限后继续");
            }
        }

        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickPhoto();
            } else {
                toastShow("请同意系统权限后继续");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }
}
