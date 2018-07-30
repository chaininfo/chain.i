package info.chain.chaini.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResult;
import com.mylhyl.zxing.scanner.OnScannerCompletionListener;
import com.mylhyl.zxing.scanner.ScannerView;
import com.mylhyl.zxing.scanner.decode.QRDecode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import info.chain.chaini.R;
import info.chain.chaini.permission.PermissionItem;
import info.chain.chaini.utils.ActivityUtils;
import info.chain.chaini.utils.FilesUtils;
import info.chain.chaini.utils.Utils;

public class ScanCodeActivity extends AppCompatActivity {

    private ScannerView scan_view;
    private ImageView iv_back;
    private TextView tv_imagebook;
    private static final int CHOOSE_PICTURE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_code);

        //
        scan_view = findViewById(R.id.scanner_view);
        scan_view.setMediaResId(R.raw.beep);
        scan_view.setLaserFrameBoundColor(getResources().getColor(R.color.black));
        scan_view.setLaserColor(getResources().getColor(R.color.black));
        scan_view.setOnScannerCompletionListener(new OnScannerCompletionListener() {
            @Override
            public void onScannerCompletion(Result result, ParsedResult parsedResult, Bitmap bitmap) {
                parseCode(parsedResult.toString());
            }
        });

        //
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //
        tv_imagebook = findViewById(R.id.tv_imagebook);
        tv_imagebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<PermissionItem> permissonItems = new ArrayList<PermissionItem>();
                permissonItems.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, getString(R.string.WRITE_STORAGE), R.drawable.permission_card1));
                permissonItems.add(new PermissionItem(Manifest.permission.READ_EXTERNAL_STORAGE, getString(R.string.READ_STORAGE), R.drawable.permission_card1));
                if(Utils.getPermissions(permissonItems , getString(R.string.SCAN_EXTERNAL_STORAGE))){
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, CHOOSE_PICTURE);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        scan_view.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        scan_view.onPause();
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ScanCodeActivity.RESULT_OK && requestCode == CHOOSE_PICTURE) {
            //获取uri拿bitmap要做压缩处理，防止oom
            ShowDialog.showDialog(this, "识别中...", true, null);
            Uri originalUri = null;
            File file = null;
            if (null != data) {
                originalUri = data.getData();
                file = FilesUtils.getFileFromMediaUri(ScanCodeActivity.this, originalUri);
            }
            Bitmap photoBmp = null;
            try {
                photoBmp = FilesUtils.getBitmapFormUri(ScanCodeActivity.this, Uri.fromFile(file));
            } catch (IOException e) {
                e.printStackTrace();
            }
            int degree = FilesUtils.getBitmapDegree(file.getAbsolutePath());
            //把图片旋转为正的方向
            Bitmap newbitmap = FilesUtils.rotateBitmapByDegree(photoBmp, degree);
            QRDecode.decodeQR(newbitmap, new OnScannerCompletionListener() {
                @Override
                public void onScannerCompletion(Result result, ParsedResult parsedResult, Bitmap bitmap) {
                    ShowDialog.dissmiss();
                    if (parsedResult != null) {
                        parseCode(parsedResult.toString());

                    } else {
                        CharSequence text = getString(R.string.scanner_error_toast);
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                        toast.show();
                    }
                }
            });
        }
    }

    public void parseCode(String data) {
        Bundle bundle = new Bundle();
        if (data.toString().contains("account_priviate_key_QRCode")) {
            if (getIntent().getStringExtra("from").equals("import_account")) {
                /*
                QrCodeAccountPrivateKeyBean qrCodeAccountPrivateKeyBean = (QrCodeAccountPrivateKeyBean) JsonUtil.parseStringToBean(data.toString(), QrCodeAccountPrivateKeyBean.class);
                bundle.putString("account_name", qrCodeAccountPrivateKeyBean.getAccount_name());
                bundle.putString("owner_private_key", qrCodeAccountPrivateKeyBean.getOwner_private_key());
                bundle.putString("active_private_key", qrCodeAccountPrivateKeyBean.getActive_private_key());
                */
                bundle.putString("scan_result", data.toString());
                ActivityUtils.goBackWithResult(ScanCodeActivity.this, 200, bundle);
            }
        } else if (data.toString().contains("make_collections_QRCode")) {
            if (getIntent().getStringExtra("from").equals("transfer")) {
                /*
                QrCodeMakeCollectionBean qrCodeMakeCollectionBean = (QrCodeMakeCollectionBean) JsonUtil.parseStringToBean(data.toString(), QrCodeMakeCollectionBean.class);
                bundle.putString("account", qrCodeMakeCollectionBean.getAccount_name());
                bundle.putString("money", qrCodeMakeCollectionBean.getMoney());
                bundle.putString("coin", qrCodeMakeCollectionBean.getCoin());
                */
                bundle.putString("scan_result", data.toString());
                ActivityUtils.goBackWithResult(ScanCodeActivity.this, 300, bundle);
            }
        } else {
            CharSequence text = getString(R.string.scan_code_notice);
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(getApplicationContext(), text, duration);
            toast.show();
        }
    }
}
