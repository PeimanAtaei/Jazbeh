package com.atisapp.jazbeh.Delegate.ui.balance;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.atisapp.jazbeh.Delegate.CheckOut.CheckOutActivity;
import com.atisapp.jazbeh.Delegate.DelegateAPIService;
import com.atisapp.jazbeh.R;
import com.atisapp.jazbeh.Storage.Prefs.IdentitySharedPref;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class BalanceFragment extends Fragment {

    private static final String TAG = "BalanceFragment";
    private Context context;
    private IdentitySharedPref identitySharedPref;
    private View balanceRoot;
    private TextView name,balance,invitationCode,purchasesCount,usersCount;
    private LinearLayout checkOutBox;
    private int currentBalance;
    private int  REQUEST_CAMERA=1,REQUEST_GALLERY=0;
    private DelegateAPIService delegateAPIService;
    private CircleImageView userAvatar;
    private Bitmap bitmap;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Log.i(TAG, "onRequestPermissionsResult: ");
        if(requestCode == REQUEST_GALLERY || requestCode == REQUEST_CAMERA)
        {
            Log.i(TAG, "onRequestPermissionsResult3: ");
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                SelectImage();
            }

            else {
                Toast.makeText(context,"شما اجازه دسترسی به گالری را به اپلیکیشن حورا طب نداده اید",Toast.LENGTH_LONG).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK && data != null)
        {

            if(requestCode == REQUEST_GALLERY)
            {
                Uri filePath = data.getData();
                try {
                    InputStream inputStream = context.getContentResolver().openInputStream(filePath);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    userAvatar.setImageBitmap(bitmap);
                    identitySharedPref.setAvatarKey(ImageToString(bitmap));

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }else if(requestCode == REQUEST_CAMERA)
            {
                try {
                    Bundle bundle = data.getExtras();
                    bitmap = (Bitmap) bundle.get("data");
                    userAvatar.setImageBitmap(bitmap);
                    identitySharedPref.setAvatarKey(ImageToString(bitmap));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        balanceRoot = inflater.inflate(R.layout.fragment_balance, container, false);
        SetupViews();

        checkOutBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, CheckOutActivity.class);
                intent.putExtra("balance",currentBalance);
                startActivity(intent);
            }
        });

        userAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA
                },REQUEST_CAMERA);
            }
        });

        return balanceRoot;
    }

    private void SetupViews()
    {
        context = getContext();
        identitySharedPref = new IdentitySharedPref(context);
        name = balanceRoot.findViewById(R.id.delegate_dashboard_name);
        invitationCode = balanceRoot.findViewById(R.id.delegate_dashboard_invitation_code);
        balance = balanceRoot.findViewById(R.id.delegate_dashboard_balance);
        purchasesCount = balanceRoot.findViewById(R.id.delegate_dashboard_purchases_count);
        usersCount = balanceRoot.findViewById(R.id.delegate_dashboard_users_count);
        checkOutBox = balanceRoot.findViewById(R.id.delegate_dashboard_check_box);
        userAvatar = balanceRoot.findViewById(R.id.balance_header_avatar);

        delegateAPIService = new DelegateAPIService(context);

        GetDashboard();
        UploadAvatar();
    }

    private void GetDashboard()
    {
        delegateAPIService.GetDelegateDashboard(new DelegateAPIService.onGetDelegateDashboard() {
            @Override
            public void onGet(BalanceModel dashboardModel) {
                currentBalance = dashboardModel.getBalance();
                name.setText(dashboardModel.getName());
                invitationCode.setText(dashboardModel.getInvitationCode()+"");
                balance.setText(dashboardModel.getBalance()+"");
                purchasesCount.setText(dashboardModel.getPurchasesCount()+" مورد ");
                usersCount.setText(dashboardModel.getUsersCount()+" نفر ");
            }
        });
    }

    private void SelectImage()
    {
        final CharSequence[] items = {"Gallery","Camera","Cancel"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Select Your Avatar");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(items[which].equals("Gallery")){
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent,"Select file"),REQUEST_GALLERY);
                }else if(items[which].equals("Camera")){
                    Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,REQUEST_CAMERA);
                }else {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }

    private String ImageToString(Bitmap bitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes,Base64.DEFAULT);
        return encodedImage;
    }

    private Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    private void UploadAvatar()
    {
        if(identitySharedPref.getAvatarKey().length()>5) {
            bitmap = StringToBitMap(identitySharedPref.getAvatarKey());
            userAvatar.setImageBitmap(bitmap);
        }

        /*profileAPIService.UploadAvatar(imageData, new ProfileAPIService.onUploadAvatar() {
            @Override
            public void onGet(boolean msg, String url) {
                Log.i(TAG, "onGet:"+url);
            }
        });*/
    }

}
