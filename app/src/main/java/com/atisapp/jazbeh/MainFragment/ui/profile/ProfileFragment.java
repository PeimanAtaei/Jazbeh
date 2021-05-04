package com.atisapp.jazbeh.MainFragment.ui.profile;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.atisapp.jazbeh.Core.MainActivity;
import com.atisapp.jazbeh.MainFragment.HomeActivity;
import com.atisapp.jazbeh.MainFragment.ui.Shop.ui.main.SectionsPagerAdapter;
import com.atisapp.jazbeh.MainFragment.ui.home.WalletApiService;
import com.atisapp.jazbeh.Favorite.FavoriteActivity;
import com.atisapp.jazbeh.Offline.OfflineListActivity;
import com.atisapp.jazbeh.R;
import com.atisapp.jazbeh.Storage.Prefs.IdentitySharedPref;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";
    private static final int    ACTIVITYNUM = 4;
    private Context context;
    private IdentitySharedPref identitySharedPref;
    private ProfileInfoApiService profileInfoApiService;
    private EditText name,code,age,location;
    private Button saveBtn;
    private LinearLayout fileUpdateBtn,wallerChargeBtn,passwordUpdateBtn,favoriteBtn,databaseBtn;
    private TextView profile_phone_number, profile_title,profileBalanceText;
    private WalletApiService walletApiService;
    private Toolbar profileToolbar;
    private View profileRoot;
    private ImageView locIcon;
    private boolean isRegistered;
    private HomeActivity homeActivity;
    private CircleImageView userAvatar;
    private Bitmap bitmap;
    private int  REQUEST_CAMERA=1,REQUEST_GALLERY=0,PLACE_PICKER_REQUEST=2,REQUEST_AVATAR=3;


    @Override
    public void onResume() {
        super.onResume();
        SetupViews();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Log.i(TAG, "onRequestPermissionsResult: ");
        if(requestCode == REQUEST_GALLERY || requestCode == REQUEST_CAMERA)
        {
            Log.i(TAG, "onRequestPermissionsResult3: ");
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
               /* Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"تصویر حساب کاربری خود را انتخاب نمایید"),CODE_GALLERY_REQUEST);
            */
               SelectImage();

            }

            else {
                Toast.makeText(context,"شما اجازه دسترسی به گالری را به اپلیکیشن جذبه نداده اید",Toast.LENGTH_LONG).show();
            }
            return;
        }else if(requestCode == PLACE_PICKER_REQUEST)
        {
            Log.i(TAG, "onRequestPermissionsResult3: ");
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
               /* Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"تصویر حساب کاربری خود را انتخاب نمایید"),CODE_GALLERY_REQUEST);
            */
                OpenPlacePicker();

            }
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
            }else if(requestCode == PLACE_PICKER_REQUEST)
            {
                Place place = PlacePicker.getPlace(data,getActivity());
                final CharSequence name = place.getName();
                final LatLng latLng = place.getLatLng();
                Log.i(TAG, "onActivityResult: "+latLng.latitude);
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());

                try {
                    List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    Address obj = addresses.get(0);
                    String add = obj.getAddressLine(0);
                    add = add + "\n" + obj.getCountryName();
                    add = add + "\n" + obj.getCountryCode();
                    add = add + "\n" + obj.getAdminArea();
                    add = add + "\n" + obj.getPostalCode();
                    add = add + "\n" + obj.getSubAdminArea();
                    add = add + "\n" + obj.getLocality();

                    Log.i(TAG, "Address" + add);
                    // Toast.makeText(this, "Address=>" + add,
                    // Toast.LENGTH_SHORT).show();
                    location.setText(obj.getSubAdminArea()+" / "+obj.getCountryName());
                    identitySharedPref.setCity(obj.getSubAdminArea()+" / "+obj.getCountryName());

                    // TennisAppActivity.showDialog(add);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        profileRoot = inflater.inflate(R.layout.activity_profile, container, false);

        SetupViews();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(homeActivity.CheckRegistration())
                {
                    if(age.getText().toString().equals("") || name.getText().toString().equals(""))
                    {
                        Toast.makeText(context, "نام و سن خود را به شکل کامل وارد نمایید" ,Toast.LENGTH_LONG).show();
                    }else {
                        UserModel userModel = new UserModel();
                        userModel.setAge(Integer.parseInt(age.getText().toString()));
                        userModel.setFullName(name.getText().toString());
                        if(code.getText().length()>0)
                            userModel.setInvitationCode(Integer.parseInt(code.getText().toString()));
                        SendUserInfo(userModel);
                    }
                }
            }

        });

        fileUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(homeActivity.CheckRegistration())
                {
                Intent intent = new Intent(context, UpdateFileActivity.class);
                startActivity(intent);
            }}
        });

        passwordUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(homeActivity.CheckRegistration())
                {
                Intent intent = new Intent(context, UpdatePasswordActivity.class);
                startActivity(intent);
            }}
        });

        wallerChargeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(homeActivity.CheckRegistration())
                {
                OpenDialogWallet();
            }}
        });

        favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(homeActivity.CheckRegistration())
                {
                Intent intent = new Intent(context, FavoriteActivity.class);
                startActivity(intent);
            }}
        });

        databaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(homeActivity.CheckRegistration()) {
                    Intent intent = new Intent(context, OfflineListActivity.class);
                    startActivity(intent);
                }
            }
        });

        userAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: avatar");
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA
                },REQUEST_CAMERA);
            }
        });

        locIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION
                },PLACE_PICKER_REQUEST);

            }
        });



        return profileRoot;
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

    private void SetupViews()
    {
        context = getContext();
        identitySharedPref = new IdentitySharedPref(context);
        profileInfoApiService = new ProfileInfoApiService(context);
        walletApiService = new WalletApiService(context);
        profile_phone_number = (TextView) profileRoot.findViewById(R.id.profile_phone_number);
        profile_title = (TextView) profileRoot.findViewById(R.id.profile_name);
        name = (EditText) profileRoot.findViewById(R.id.profile_name_edit_text);
        code = (EditText) profileRoot.findViewById(R.id.profile_invitation_edit);
        location =  profileRoot.findViewById(R.id.profile_location_edit);
        userAvatar =  profileRoot.findViewById(R.id.avatar_image);
        locIcon =  profileRoot.findViewById(R.id.profile_location_image);
        age = (EditText) profileRoot.findViewById(R.id.profile_age_edit);
        saveBtn = (Button) profileRoot.findViewById(R.id.profile_save_btn);
        fileUpdateBtn = (LinearLayout) profileRoot.findViewById(R.id.profile_file_update_btn);
        passwordUpdateBtn = (LinearLayout) profileRoot.findViewById(R.id.profile_password_update_btn);
        wallerChargeBtn = (LinearLayout) profileRoot.findViewById(R.id.profile_wallet_charge_btn);
        favoriteBtn = (LinearLayout) profileRoot.findViewById(R.id.profile_favorite_btn);
        databaseBtn = (LinearLayout) profileRoot.findViewById(R.id.profile_database_btn);
        profileBalanceText = profileRoot.findViewById(R.id.profile_balance_text);
        homeActivity = (HomeActivity)getActivity();

        CheckRegister();
        UploadAvatar();

    }

    private void SetupData(UserModel userModel)
    {

        name.setText(userModel.getFullName());
        profile_title.setText(userModel.getFullName());
        if(userModel.getAge() != 0)
            age.setText(userModel.getAge()+"");
        if(userModel.getInvitationCode() != 0)
            code.setText(userModel.getInvitationCode()+"");
        profile_phone_number.setText(userModel.getPhoneNumber());
        if(identitySharedPref.getCity().length()>5)
            location.setText(identitySharedPref.getCity());

        identitySharedPref.setPhoneNumber(userModel.getPhoneNumber());
        identitySharedPref.setName(userModel.getFullName());
        identitySharedPref.setAge(userModel.getAge());
        identitySharedPref.setInvitationCode(userModel.getInvitationCode());

    }

    private void GetUserInfo()
    {
        profileInfoApiService.ReceivePersonalInfo(new ProfileInfoApiService.onReceivePersonalInfoResponse() {
            @Override
            public void onGet(boolean response, UserModel userModel) {
                if(response)
                {
                    SetupData(userModel);
                    //Toast.makeText(context,"",Toast.LENGTH_SHORT);
                }
                else {
                    Toast.makeText(context,"خطا در بروز رسانی اطلاعات",Toast.LENGTH_SHORT);
                }
            }
        });
    }

    private void SendUserInfo(UserModel myUserModel)
    {
        profileInfoApiService.SendPersonalInfo(myUserModel, new ProfileInfoApiService.onGetAddPersonalInfoResponse() {
            @Override
            public void onGet(boolean response, UserModel userModel) {
                if(response)
                {
                    GetUserInfo();
                    Toast.makeText(context,"اطلاعات شما با موفقیت بروز رسانی شد",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context,"خطا در بروز رسانی اطلاعات",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void GetWalletBalance()
    {
        walletApiService.showBalance(new WalletApiService.OnGetBalanceListener() {
            @Override
            public void onBalance(int balance) {
                identitySharedPref.setWalletBalance(balance);
                profileBalanceText.setText(" اعتبار فعلی شما "+balance+" تومان می باشد ");
            }
        });
    }

    private void OpenDialogWallet() {
        ViewGroup viewGroup = profileRoot.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.layout_wallet, viewGroup, false);

        TextView balanceLabel = dialogView.findViewById(R.id.balanceLabel);
        final EditText balanceEditText = dialogView.findViewById(R.id.balanceEditText);
        final Button chargeButtonOne = dialogView.findViewById(R.id.chargeButtonOne);
        final Button chargeButtonTwo = dialogView.findViewById(R.id.chargeButtonTwo);
        final Button chargeButtonThree = dialogView.findViewById(R.id.chargeButtonThree);
        Button balanceChargeButton = dialogView.findViewById(R.id.balanceChargeButton);

        String balanceValue = "اعتبار فعلی شما : " + convertNumberToBalance(identitySharedPref.getWalletBalance()) + " تومان ";
        balanceLabel.setText(balanceValue);

        chargeButtonOne.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                //view.setBackground(getResources().getDrawable(R.drawable.selector_input_selected));
                //chargeButtonTwo.setBackground(getResources().getDrawable(R.drawable.selector_input));
                //chargeButtonThree.setBackground(getResources().getDrawable(R.drawable.selector_input));
                balanceEditText.setText(getString(R.string.wallet_charge_1));
            }
        });

        chargeButtonTwo.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                //view.setBackground(getResources().getDrawable(R.drawable.selector_input_selected));
                //chargeButtonOne.setBackground(getResources().getDrawable(R.drawable.selector_input));
                //chargeButtonThree.setBackground(getResources().getDrawable(R.drawable.selector_input));
                balanceEditText.setText(getString(R.string.wallet_charge_2));
            }
        });

        chargeButtonThree.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                //view.setBackground(getResources().getDrawable(R.drawable.selector_input_selected));
                //chargeButtonTwo.setBackground(getResources().getDrawable(R.drawable.selector_input));
                //chargeButtonOne.setBackground(getResources().getDrawable(R.drawable.selector_input));
                balanceEditText.setText(getString(R.string.wallet_charge_3));
            }
        });

        balanceChargeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String balance = balanceEditText.getText().toString().replaceAll(",", "");
                if (!TextUtils.isEmpty(balance)) {
                    hideKeyboard(balanceEditText);
                    ChargeWallet(balance);
                } else {
                    Toast.makeText(context, "لطفا مقدار اعتبار خود را وارد نمایید", Toast.LENGTH_SHORT).show();
                }
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setView(dialogView);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private String convertNumberToBalance(int balance) {
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        return formatter.format(balance);
    }

    public void hideKeyboard(EditText myEditText) {
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(myEditText.getWindowToken(), 0);
        }
    }

    private void ChargeWallet(String balance) {

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("در حال اتصال به درگاه");
        progressDialog.show();

        walletApiService = new WalletApiService(context.getApplicationContext());
        walletApiService.ChargeWallet(balance, new WalletApiService.OnPayListener() {

            @Override
            public void onPay(String url) {
                progressDialog.dismiss();
                openBrowser(url);
            }

            @Override
            public void onError(int statusCode) {
                progressDialog.dismiss();
            }
        });
    }

    private void openBrowser(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    private void CheckRegister()
    {
        if(homeActivity.CheckRegistration())
        {
            isRegistered = true;
            GetUserInfo();
            GetWalletBalance();
        }else {
            isRegistered = false;
        }
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
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
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

    private void OpenPlacePicker()
    {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        
        try {
            Intent intent = builder.build(getActivity());
            startActivityForResult(intent,PLACE_PICKER_REQUEST);

        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

}
