package com.atisapp.jazbeh.Delegate.ui.information;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.atisapp.jazbeh.Delegate.DelegateAPIService;
import com.atisapp.jazbeh.Delegate.DelegateModel;
import com.atisapp.jazbeh.Delegate.RulesActivity;
import com.atisapp.jazbeh.R;
import com.atisapp.jazbeh.Storage.Prefs.IdentitySharedPref;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class InformationFragment extends Fragment {

    private static final String TAG = "InformationFragment";
    private Context context;
    private IdentitySharedPref identitySharedPref;
    private DelegateAPIService delegateAPIService;
    private View infoRoot;
    private EditText name,city,education,card,collaboration;
    private Button rulesBtn,sendBtn;
    private DelegateModel delegateModel;
    private ImageView locIcon;
    private int  PLACE_PICKER_REQUEST=2;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Log.i(TAG, "onRequestPermissionsResult: ");
        if(requestCode == PLACE_PICKER_REQUEST)
        {
            Log.i(TAG, "onRequestPermissionsResult3: ");
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
               /* Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"تصویر حساب کاربری خود را انتخاب نمایید"),CODE_GALLERY_REQUEST);
            */
                OpenPlacePicker();

            }
            else {
                Toast.makeText(context,"شما اجازه دسترسی به گالری را به اپلیکیشن حورا طب نداده اید",Toast.LENGTH_LONG).show();
            }
        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK && data != null)
        {

            if(requestCode == PLACE_PICKER_REQUEST)
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
                    city.setText(obj.getSubAdminArea()+" / "+obj.getCountryName());
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

        infoRoot = inflater.inflate(R.layout.fragment_information, container, false);
        SetupViews();

        rulesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RulesActivity.class);
                startActivity(intent);
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().length()>0 && city.getText().length()>0 && education.getText().length()>0 && card.getText().length()>0 && collaboration.getText().length()>0)
                {
                    ModifyInformation();
                }else {
                    Toast.makeText(context,"تمام اطلاعات فرم را تکمیل نمایید",Toast.LENGTH_LONG).show();
                }
            }
        });
        locIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION
                },PLACE_PICKER_REQUEST);

            }
        });

        return infoRoot;


    }

    private void SetupViews()
    {
        context = getContext();
        delegateAPIService = new DelegateAPIService(context);
        identitySharedPref = new IdentitySharedPref(context);

        name = infoRoot.findViewById(R.id.delegate_info_fullName_edit);
        city = infoRoot.findViewById(R.id.delegate_info_city_edit);
        education = infoRoot.findViewById(R.id.delegate_info_education_edit);
        card = infoRoot.findViewById(R.id.delegate_info_card_edit);
        collaboration = infoRoot.findViewById(R.id.delegate_info_channel_edit);
        locIcon = infoRoot.findViewById(R.id.information_location_image);

        rulesBtn = infoRoot.findViewById(R.id.delegate_info_rules_btn);
        sendBtn = infoRoot.findViewById(R.id.delegate_info_send_btn);

        GetInformation();
    }

    private void GetInformation()
    {
        delegateAPIService.GetDelegateInfo(new DelegateAPIService.onGetInfo() {
            @Override
            public void onGet(DelegateModel delegateModelList) {
                if(delegateModelList != null)
                {
                    name.setText(delegateModelList.getName());
                    if(identitySharedPref.getCity().length()>5)
                        city.setText(identitySharedPref.getCity());
                    else
                        city.setText(delegateModelList.getCity());
                    education.setText(delegateModelList.getEducation());
                    card.setText(delegateModelList.getCardNumber());
                    collaboration.setText(delegateModelList.getCollaborate());
                }
            }
        });
    }

    private void ModifyInformation()
    {
        delegateModel = new DelegateModel();

        delegateModel.setName(name.getText().toString());
        delegateModel.setCity(city.getText().toString());
        delegateModel.setEducation(education.getText().toString());
        delegateModel.setCardNumber(card.getText().toString());
        delegateModel.setCollaborate(collaboration.getText().toString());

        delegateAPIService.ModifyDelegateInformation(delegateModel, new DelegateAPIService.onCreateNewDelegate() {
            @Override
            public void onGet(boolean response) {
                Toast.makeText(context,"بروز رسانی اطلاعات با موفقیت انجام شد",Toast.LENGTH_LONG).show();
            }
        });
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


