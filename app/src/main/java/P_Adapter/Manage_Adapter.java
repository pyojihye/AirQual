package P_Adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.pyojihye.airpollution.R;
import com.example.pyojihye.airpollution.activity.MainActivity;

import P_Data.Preference_Data;
import P_Data.Util_STATUS;
import P_Fragment.Fr_DeviceManagement;

/**
 * Created by user on 2016-08-10.
 */
public class Manage_Adapter extends PagerAdapter{

    LayoutInflater inflater;
    public static Context context;
    public Handler Manage_handler; // 이 핸들러로 메인 액티비티에 블루투스 붙일거임
    private static Manage_Adapter instance;
    public static ImageView HEARTImageview;
    public static ImageView UDOOImageview;
    public static TextView  UDOONAMETextview;
    public static TextView  UDOOMACTextview;
    public static TextView  HEARTNAMETextview;
    public static TextView  HEARTMACTEXTview;
    public static ToggleButton HEARTButton;
    public static ToggleButton UDOOButton;


    public static Manage_Adapter getInstance() {
        return instance;
    }


    public Manage_Adapter(LayoutInflater inflater, Context context,Handler handler)
    {
        this.inflater=inflater;
        this.context=context;
        this.Manage_handler=handler;
        instance = this;
    }

    @Override
    public int getCount() {
        return Util_STATUS.HELP_VIEW_COUNT;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = null;
      /* 여기에 onclick listener 등록*/
        if (Util_STATUS.HELP_VIEW == false) //아직 도움말이 안나왔으면
        {
            switch (position) {
                case 0: {
                    //view=inflater.inflate(R.layout.air_status,null);
                    view = inflater.inflate(R.layout.fr_device_management_help, null);
                    container.addView(view);
                    Button btn = (Button) view.findViewById(R.id.buttonNext);
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (Fr_DeviceManagement.getInstance() != null) {
                                Fr_DeviceManagement.v_viewpager.setCurrentItem(1);

                            }
                        }
                    });

                    break;
                }
                case 1: {
                    view = inflater.inflate(R.layout.fr_device_management, null);

                    Preference_Data preference_data = new Preference_Data(MainActivity.getInstance());

                    UDOOImageview = (ImageView) view.findViewById(R.id.imageViewUdoo);
                    UDOOImageview.setOnClickListener(imageClickListener);
                    HEARTImageview = (ImageView) view.findViewById(R.id.imageViewHeart);
                    HEARTImageview.setOnClickListener(imageClickListener);
                    UDOOMACTextview = (TextView) view.findViewById(R.id.TextViewUDOOMac);

                    UDOONAMETextview = (TextView) view.findViewById(R.id.TextViewUDOO);
                    HEARTMACTEXTview = (TextView) view.findViewById(R.id.TextViewHRMac);
                    HEARTNAMETextview = (TextView) view.findViewById(R.id.TextViewHR);
                    UDOOButton = (ToggleButton) view.findViewById(R.id.toggleButtonUdoo);
                    UDOOButton.setOnClickListener(ToggleClickListener);

                    HEARTButton = (ToggleButton) view.findViewById(R.id.toggleButtonHeart);
                    HEARTButton.setOnClickListener(ToggleClickListener);
                    if (!preference_data.get_data("HEARTMAC").equals("") || !preference_data.get_data("HEARTNAME").equals("")) {
                        HEARTMACTEXTview.setText(preference_data.get_data("HEARTMAC"));
                        HEARTNAMETextview.setText(preference_data.get_data("HEARTNAME"));
                    } else if (preference_data.get_data("HEARTMAC").equals("") || preference_data.get_data("HEARTNAME").equals("")) {
                        HEARTNAMETextview.setText("Heart Rate");
                        HEARTMACTEXTview.setText("NOT CONNECTED");
                    }
                    if (!preference_data.get_data("UDOOMAC").equals("") || !preference_data.get_data("UDOONAME").equals("")) {

                        UDOONAMETextview.setText(preference_data.get_data("UDOONAME"));
                        UDOOMACTextview.setText(preference_data.get_data("UDOOMAC"));


                    } else if (preference_data.get_data("UDOOMAC").equals("") || preference_data.get_data("UDOONAME").equals("")) {
                        UDOONAMETextview.setText("UDOO BOARD");
                        UDOOMACTextview.setText("NOT CONNECTED");

                    }
                    Button btn=(Button)view.findViewById(R.id.buttonNext);
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                        MainActivity.getInstance().Go_to_Main();
                        }
                    });
                    container.addView(view);
                    Util_STATUS.HELP_VIEW=true;
                    break;
                }

            }
        } else if (Util_STATUS.HELP_VIEW == true) {
            switch (position) {
                case 0:
                {
                    Util_STATUS.HELP_VIEW_COUNT=2;
                    view = inflater.inflate(R.layout.fr_device_management, null);

                    Preference_Data preference_data = new Preference_Data(MainActivity.getInstance());
                    Button btn=(Button)view.findViewById(R.id.buttonNext);
                    btn.setVisibility(View.GONE);
                    UDOOImageview = (ImageView) view.findViewById(R.id.imageViewUdoo);
                    UDOOImageview.setOnClickListener(imageClickListener);
                    HEARTImageview = (ImageView) view.findViewById(R.id.imageViewHeart);
                    HEARTImageview.setOnClickListener(imageClickListener);
                    UDOOMACTextview = (TextView) view.findViewById(R.id.TextViewUDOOMac);

                    UDOONAMETextview = (TextView) view.findViewById(R.id.TextViewUDOO);
                    HEARTMACTEXTview = (TextView) view.findViewById(R.id.TextViewHRMac);
                    HEARTNAMETextview = (TextView) view.findViewById(R.id.TextViewHR);
                    UDOOButton = (ToggleButton) view.findViewById(R.id.toggleButtonUdoo);
                    UDOOButton.setOnClickListener(ToggleClickListener);

                    HEARTButton = (ToggleButton) view.findViewById(R.id.toggleButtonHeart);
                    HEARTButton.setOnClickListener(ToggleClickListener);
                    if (!preference_data.get_data("HEARTMAC").equals("") || !preference_data.get_data("HEARTNAME").equals("")) {
                        HEARTMACTEXTview.setText(preference_data.get_data("HEARTMAC"));
                        HEARTNAMETextview.setText(preference_data.get_data("HEARTNAME"));
                    } else if (preference_data.get_data("HEARTMAC").equals("") || preference_data.get_data("HEARTNAME").equals("")) {
                        HEARTNAMETextview.setText("Heart Rate");
                        HEARTMACTEXTview.setText("NOT CONNECTED");
                    }
                    if (!preference_data.get_data("UDOOMAC").equals("") || !preference_data.get_data("UDOONAME").equals("")) {

                        UDOONAMETextview.setText(preference_data.get_data("UDOONAME"));
                        UDOOMACTextview.setText(preference_data.get_data("UDOOMAC"));


                    } else if (preference_data.get_data("UDOOMAC").equals("") || preference_data.get_data("UDOONAME").equals("")) {
                        UDOONAMETextview.setText("UDOO BOARD");
                        UDOOMACTextview.setText("NOT CONNECTED");

                    }
                    container.addView(view);
                    Util_STATUS.HELP_VIEW=true;

                    //연결이 되있으면 UDOO 이미지 변경 , UDOO NAME UDOO MAC 변경 UDOO BUTTON 변경

                    if(Util_STATUS.connect_udoo==true) //UDOO가 연결되있으면
                    {
                        UDOOImageview.setImageResource(R.drawable.udoo);
                        UDOOMACTextview.setText(preference_data.get_data("UDOOMAC"));
                        UDOONAMETextview.setText(preference_data.get_data("UDOONAME"));
                        UDOOButton.setChecked(true);
                    }
                    if(Util_STATUS.connect_ble==true) //ble가 연결되있으면
                    {
                        HEARTImageview.setImageResource(R.drawable.heartrate);
                        HEARTMACTEXTview.setText(preference_data.get_data("HEARTMAC"));
                        HEARTNAMETextview.setText(preference_data.get_data("HEARTNAME"));
                        HEARTButton.setChecked(true);
                    }

                    break;

                }
            }

        }
        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return  view==object;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


    //ImageView 클릭 UDOO OR HEARTRATE
    ImageView.OnClickListener imageClickListener= new ImageView.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId())
            {
                //ImageViewHeart 니까 디바이스 등록 요청 디바이스 기기검색 요청해야함
                //HTTP CONNECTION 요청은 왠만하면 메인에서
                //UDOO 등록 요청
                case R.id.imageViewUdoo:
                {
                    Util_STATUS.SELECT_DEVICE=0;
                    Manage_handler.obtainMessage(Util_STATUS.REQUEST_UDOO_DEVICE_REGISTER).sendToTarget();
                    break;
                }
                case R.id.imageViewHeart:
                {
                    Util_STATUS.SELECT_DEVICE=1;
                    Manage_handler.obtainMessage(Util_STATUS.REQUEST_HEART_DEVICE_REGISTER).sendToTarget();
                    break;
                }
            }

        }
    };

    // TOGGLE BUTTON CLICK
    ToggleButton.OnClickListener ToggleClickListener=new ToggleButton.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId())
            {
                //우도 버튼이면
                case R.id.toggleButtonUdoo:
                {
                    if(UDOOMACTextview.getText().equals("NOT CONNECTED"))
                    {
                        break;
                    }
                    if(!UDOOButton.isChecked()) // 읽고난 다음인거같으니 false일때가 사용으로 간주
                    {

                        Util_STATUS.SELECT_DEVICE=0;
                        Manage_handler.obtainMessage(Util_STATUS.REQUEST_UDOO_DISCONNECT).sendToTarget();
                    }
                    else if(UDOOButton.isChecked())
                    {
                        Util_STATUS.SELECT_DEVICE=0;
                        Manage_handler.obtainMessage(Util_STATUS.REQUEST_UDOO_CONNECT).sendToTarget();

                    }
                    break;
                }
                case R.id.toggleButtonHeart:
                {
                    if(HEARTMACTEXTview.getText().equals("NOT CONNECTED"))
                    {
                        break;
                    }
                    if(!HEARTButton.isChecked()) // 읽고난 다음인거같으니 false일때가 사용으로 간주
                    {
                        Util_STATUS.SELECT_DEVICE=1;
                        Manage_handler.obtainMessage(Util_STATUS.REQUEST_HEART_DISCONNECT).sendToTarget();
                    }
                    else if(HEARTButton.isChecked())
                    {
                        Util_STATUS.SELECT_DEVICE=1;
                        Manage_handler.obtainMessage(Util_STATUS.REQUEST_HEART_CONNECT).sendToTarget();
                    }
                    break;
                }

            }
        }
    };
}
