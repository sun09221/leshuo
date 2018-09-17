package com.example.feng.xgs.main.mine.dating;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.activitys.PermissionActivity;
import com.example.feng.xgs.base.recycler.ItemTypeKeys;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;
import com.example.feng.xgs.config.key.CodeKeys;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.config.key.ParameterDatingKeys;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.config.key.ShareKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.loader.NaturalLoader;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.callback.IError;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.main.mine.dating.edit.EditInfoActivity;
import com.example.feng.xgs.main.mine.dating.edit.LabelSelectActivity;
import com.example.feng.xgs.utils.UploadImgUtil;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;
import com.example.feng.xgs.utils.file.UriUtils;
import com.example.feng.xgs.utils.manager.LogUtils;
import com.example.feng.xgs.utils.manager.ToastUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by feng on 2018/5/10 0010.
 * 我的交友资料
 */

public class DatingActivity extends PermissionActivity implements BaseQuickAdapter.OnItemClickListener, DatingAdapter.IPhotoLayoutItemListener {

    @BindView(R.id.rv_dating)
    RecyclerView mRecyclerView;
    private DatingDataConverter mDataConverter;
    private DatingAdapter mAdapter;
    private int mPosition = -1, mItemType = -1;
    private String mInfo;
    private String mAnswer;


    //相册上传相关
    private ImageView mImageView;
    private String[] mPhotoArray = new String[]{"", "", "", "", "", ""};
    private boolean isUploading = false;//是否正在上传图片
    private int mPositionPhoto;


    @Override
    public Object setLayout() {
        return R.layout.activity_mine_dating;
    }

    @Override
    public void onBindView(Bundle savedInstanceState) {
        setTitle(getString(R.string.mine_make_friend));

        initRecyclerView();

        loadData();

//        getLabelFirst();
    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        mDataConverter = new DatingDataConverter();
        mAdapter = new DatingAdapter(mDataConverter.ENTITIES);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(this);
//        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setPhotoLayoutItemListener(this);
    }

    /**
     * 电影 id =2;
     * 旅行 id =3;
     * 食物 id =4;
     * 书和漫画 id =5;
     * 音乐 id =6;
     * 运动 id =7;
     */
    private void getLabelFirst() {
        RestClient.builder()
                .url(UrlKeys.LABEL_FIRST)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {

                    }
                })
                .build().post();
    }


    private void loadData() {
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.ID_ONLY, SharedPreferenceUtils.getPeopleId());
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.USER_INFO)
                .raw(raw)
                .loader(this)
                .toast()
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        List<BaseEntity> entities = mDataConverter.setJsonData(response).convert();
                        mAdapter.setNewData(entities);

                        String photoUrls = entities.get(0).getField(EntityKeys.IMG_URL_S);
                        initPhotoArray(photoUrls);
                    }
                })
                .build().post();
    }

    private void initPhotoArray(String photoUrls) {
        if (!TextUtils.isEmpty(photoUrls)) {
            String[] photoArray = photoUrls.split(",");
            for (int i = 0; i < photoArray.length; i++) {
                if (i < mPhotoArray.length) {
                    mPhotoArray[i] = photoArray[i];
                }
            }
        }
    }


    /**
     * 由于布局是固定的，可以根据position判断 是什么功能
     * <p>
     * icon_natural_publish_address = 3： 个性签名
     * icon_natural_publish_address = 6-10：我的信息
     * icon_natural_publish_address = 13：我的标签
     * icon_natural_publish_address = 16-21：我的兴趣
     * icon_natural_publish_address = 24： 星座
     * icon_natural_publish_address = 27： 问题
     */
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Log.d(TAG, "onItemClick: " + position);
        Log.d(TAG, "onItemClick: " + adapter.getItemViewType(position));
        mPosition = position;
        //如果info 不为空传到下一个页面，编辑
        mInfo = mAdapter.getData().get(position).getField(EntityKeys.INFO);
        switch (position) {
            //个性签名
            case 3:
                mItemType = ItemTypeKeys.MINE_DATING_IMG_WITH_TEXT;
                startEditInfoActivity("个性签名");
                break;

            case 6:
                mItemType = ItemTypeKeys.MINE_DATING_IMG_WITH_TEXT;
                startLabelSelectActivity(DatingLabelKeys.DATING_LABEL_ID_INDUSTRY, "行业信息",
                        true, false);
                break;
            case 7:
                mItemType = ItemTypeKeys.MINE_DATING_IMG_WITH_TEXT;
                startLabelSelectActivity(DatingLabelKeys.DATING_LABEL_ID_JOBAREA, "工作领域",
                        true, false);
                break;
            case 8:
                mItemType = ItemTypeKeys.MINE_DATING_IMG_WITH_TEXT;
                startEditInfoActivity("公司名称");
                break;
            case 9:
                mItemType = ItemTypeKeys.MINE_DATING_IMG_WITH_TEXT;
                startLabelSelectActivity(DatingLabelKeys.DATING_LABEL_ID_HOMTOEN, "家乡信息",
                        true, true);
                break;
            case 10:
                mItemType = ItemTypeKeys.MINE_DATING_IMG_WITH_TEXT;
                startEditInfoActivity("常去地点");
                break;

            case 13:
                mItemType = ItemTypeKeys.MINE_DATING_LABEL;
                startLabelSelectActivity(DatingLabelKeys.DATING_LABEL_ID_LABEL, "我的标签",
                        false, true);
                break;
            case 16:
                mItemType = ItemTypeKeys.MINE_DATING_LABEL;
                startLabelSelectActivity(DatingLabelKeys.DATING_LABEL_ID_SPORTS, "运动",
                        false, true);
                break;

            case 17:
                mItemType = ItemTypeKeys.MINE_DATING_LABEL;
                startLabelSelectActivity(DatingLabelKeys.DATING_LABEL_ID_MUSIC, "音乐",
                        false, true);
                break;
            case 18:
                mItemType = ItemTypeKeys.MINE_DATING_LABEL;
                startLabelSelectActivity(DatingLabelKeys.DATING_LABEL_ID_FOOD, "食物",
                        false, true);
                break;
            case 19:
                mItemType = ItemTypeKeys.MINE_DATING_LABEL;
                startLabelSelectActivity(DatingLabelKeys.DATING_LABEL_ID_MOVIE, "电影",
                        false, true);
                break;
            case 20:
                mItemType = ItemTypeKeys.MINE_DATING_LABEL;
                startLabelSelectActivity(DatingLabelKeys.DATING_LABEL_ID_BOOKS, "我的阅读",
                        false, true);
                break;
            case 21:
                mItemType = ItemTypeKeys.MINE_DATING_LABEL;
                startLabelSelectActivity(DatingLabelKeys.DATING_LABEL_ID_TRAVEL, "旅行足迹",
                        false, true);
                break;
            case 24:
                mItemType = ItemTypeKeys.MINE_DATING_LABEL;
                startLabelSelectActivity(DatingLabelKeys.DATING_LABEL_ID_STAR_SIGN, "我的星座",
                        true, false);
                break;
            case 27:

//                    mQuestionType = QUESTION_TYPE_QUESTION;
                mItemType = ItemTypeKeys.MINE_DATING_QUESTION;
                mInfo = mAdapter.getData().get(position).getField(EntityKeys.NAME);
                startLabelSelectActivity(DatingLabelKeys.DATING_LABEL_ID_QUESTION, "我的问题",
                        true, false);

                break;
            default:
                break;

        }
    }


    //跳转到信息编辑页面
    private void startEditInfoActivity(String title) {
        Intent intent = new Intent(this, EditInfoActivity.class);
        intent.putExtra(ContentKeys.ACTIVITY_TITLE, title);
        //如果info 不为空传到下一个页面，编辑
        if (!TextUtils.isEmpty(mInfo)) {
            intent.putExtra(ContentKeys.ACTIVITY_INFO, mInfo);
        }
        startActivityForResult(intent, CodeKeys.ADD_REQUEST);
    }

    /**
     * 跳转到选择标签页面
     *
     * @param idOnly       标签id
     * @param title        标题
     * @param isSelectOnly 是否单选
     * @param isCustomShow 是否可以添加自定义标签
     */
    private void startLabelSelectActivity(String idOnly, String title, boolean isSelectOnly, boolean isCustomShow) {

        Intent intent = new Intent(this, LabelSelectActivity.class);
        intent.putExtra(ContentKeys.ACTIVITY_TITLE, title);
        if (!TextUtils.isEmpty(mInfo)) {
            intent.putExtra(ContentKeys.ACTIVITY_INFO, mInfo);
        }
        intent.putExtra(ContentKeys.IS_DATING_LABEL_SELECT_ONLY, isSelectOnly);
        intent.putExtra(ContentKeys.IS_DATING_LABEL_CUSTOM_SHOW, isCustomShow);
        intent.putExtra(ContentKeys.ACTIVITY_ID_ONLY, idOnly);
        startActivityForResult(intent, CodeKeys.ADD_REQUEST);
    }


    /**
     * 由于布局是固定的，可以根据position判断 是什么功能
     * <p>
     * icon_natural_publish_address = 3： 个性签名
     * icon_natural_publish_address = 6-10：我的信息
     * icon_natural_publish_address = 13：我的标签
     * icon_natural_publish_address = 16-21：我的兴趣
     * icon_natural_publish_address = 24： 星座
     * icon_natural_publish_address = 27： 问题
     */
    private void saveInfo(Intent data) {
        final String info = data.getStringExtra(ContentKeys.ACTIVITY_INFO);
        Log.d(TAG, "onActivityResult: " + info);

        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.USER_ID, SharedPreferenceUtils.getUserId());
        Log.d(TAG, "saveInfo: " + mPosition);
        switch (mPosition) {
            case 3:
                map.put(ParameterDatingKeys.AUTOGRAPH, info);//签名
                break;
            case 6:
                map.put(ParameterDatingKeys.INDUSTRY, info);//行业信息
                break;

            case 7:
                map.put(ParameterDatingKeys.JOB_AREA, info);//工作领域
                break;
            case 8:
                map.put(ParameterDatingKeys.COMPANY, info);//公司
                break;

            case 9:
                map.put(ParameterDatingKeys.HOMETOWN, info);//家乡
                break;

            case 10:
                map.put(ParameterDatingKeys.ADDRESS, info);//常去地址
                break;

            case 13:
                map.put(ParameterDatingKeys.LABEL, info);//我的标签
                break;
            case 16:
                map.put(ParameterDatingKeys.SPORTS, info);//运动
                break;
            case 17:
                map.put(ParameterDatingKeys.MUSIC, info);//音乐
                break;
            case 18:
                map.put(ParameterDatingKeys.FOOD, info);//食物
                break;
            case 19:
                map.put(ParameterDatingKeys.FILM, info);//电影
                break;
            case 20:
                map.put(ParameterDatingKeys.BOOKS, info);//阅读
                break;
            case 21:
                map.put(ParameterDatingKeys.TRAVEL, info);//旅行
                break;
            case 24:
                map.put(ParameterDatingKeys.STAR_SIGN, info);//星座
                break;
            case 27:
                mAnswer = data.getStringExtra(ContentKeys.ACTIVITY_ANSWER);
                map.put(ParameterDatingKeys.QUESTION, info);//问题
                map.put(ParameterDatingKeys.ANSWER, mAnswer);//回答
                LogUtils.d("answer: " + mAnswer);

                break;
            default:
                break;
        }

        String raw = JSON.toJSONString(map);
        RestClient.builder()
                .url(UrlKeys.USER_INFO_EDIT)
                .raw(raw)
                .loader(this)
                .toast()
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        ToastUtils.showText(DatingActivity.this, "修改成功");

                        BaseEntity entity = mAdapter.getData().get(mPosition);
                        entity.setField(EntityKeys.ITEM_TYPE, mItemType);
                        switch (mItemType) {
                            case ItemTypeKeys.MINE_DATING_IMG_WITH_TEXT:
                                entity.setField(EntityKeys.INFO, info);
                                break;

                            case ItemTypeKeys.MINE_DATING_LABEL:
                                entity.setField(EntityKeys.INFO, info);
                                break;
                            case ItemTypeKeys.MINE_DATING_QUESTION:

                                entity.setField(EntityKeys.NAME, info);
                                entity.setField(EntityKeys.INFO, mAnswer);


                                break;
                            default:
                                break;
                        }

                        mAdapter.notifyItemChanged(mPosition);
                    }
                })
                .build().post();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == CodeKeys.FOR_RESULT_CODE) {

            if (data == null) return;
            if (mPosition == -1 || mItemType == -1) {
                return;
            }
            saveInfo(data);
        } else if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CodeKeys.CAMERA_OPEN:
                    String cameraPath = SharedPreferenceUtils.getCustomAppProfile(ShareKeys.CAMERA_FILE);
                    LogUtils.d("onActivityResult: " + cameraPath);
                    loadImg(cameraPath, mImageView);

                    upLoadImage(cameraPath);
                    break;
                case CodeKeys.PHOTO_OPEN:
                    if (data != null) {
                        Uri photoUri = data.getData();
                        LogUtils.d("uri: " + photoUri);
                        String photoPath ="";
                        if(photoUri.getScheme().equals("file")){
                            photoPath =photoUri.getPath();
                        }else {
                            photoPath = UriUtils.getPhotoPath(this, photoUri);
                        }
                        LogUtils.d("onActivityResult: " + photoPath);
                        loadImg(photoPath, mImageView);
                        upLoadImage(photoPath);
                    }

                    break;
                default:
                    break;
            }
        }
//        uri: content://com.android.providers.media.documents/document/image%3A471434
//        uri: content://com.android.providers.media.documents/document/image%3A371781
//        /storage/emulated/0/Pictures/Screenshots/Screenshot_20180622-112512.jpg
    }


    @Override
    public void onItemClick(int position, ImageView imageView) {
        if (isUploading) {
            ToastUtils.showText(this, "正在上传，请稍候...");
        } else {
            mPositionPhoto = position;
            mImageView = imageView;
            requestCameraPermission();
        }

    }

    /**
     * 上传图片
     */
    private void upLoadImage(String path) {
        NaturalLoader.showLoading(this);
        isUploading = true;
        UploadImgUtil.create()
                .setErrorListener(new UploadImgUtil.IUploadErrorListener() {
                    @Override
                    public void onError() {
                        isUploading = false;
                    }
                })
                .uploadImg(path, new UploadImgUtil.IUploadImgListener() {
                    @Override
                    public void onSuccess(String path) {
                        mPhotoArray[mPositionPhoto] = path;

                        String imgUrls = getPhotoUrls();
                        if(TextUtils.isEmpty(imgUrls)) return;

                        Map<String, String> map = new HashMap<>();
                        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
                        map.put(ParameterKeys.IMAGE_PATH, imgUrls);
                        String raw = JSON.toJSONString(map);

                        RestClient.builder()
                                .url(UrlKeys.USER_PHOTO_EDIT)
                                .raw(raw)
                                .toast()
                                .success(new ISuccess() {
                                    @Override
                                    public void onSuccess(String response) {
                                        ToastUtils.showText(DatingActivity.this, "上传成功");
                                        NaturalLoader.stopLoading();
                                        isUploading = false;
                                    }
                                })
                                .error(new IError() {
                                    @Override
                                    public void onError(int code, String message) {
                                        NaturalLoader.stopLoading();
                                        isUploading = false;
                                    }
                                })
                                .build().post();
                    }
                });

    }

    private String getPhotoUrls(){
        int size = mPhotoArray.length;
        StringBuilder builder = new StringBuilder();
        int count = 0;
        for (int i = 0; i < size; i++) {
            String imgUrl = mPhotoArray[i];
            if(!TextUtils.isEmpty(imgUrl)){
                if(count == 0){
                    builder.append(imgUrl);
                }else {
                    builder.append(",")
                            .append(imgUrl);
                }
                count++;
            }
        }

        String result = builder.toString();
        LogUtils.d("photoUrls: " + result);
        return result;
    }


}
