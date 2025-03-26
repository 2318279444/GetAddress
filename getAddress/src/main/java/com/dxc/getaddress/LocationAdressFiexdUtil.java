package com.dxc.getaddress;

import static android.view.View.VISIBLE;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.contrarywind.adapter.WheelAdapter;
import com.contrarywind.view.WheelView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LocationAdressFiexdUtil {

    private List<String> list1 = new ArrayList<>();
    private List<String> list2 = new ArrayList<>();
    private List<String> list3 = new ArrayList<>();
    private String strProvince = "";
    private String strCity = "";
    private WheelView wheeviewProvince;
    private WheelView wheeviewCity;
    private WheelView wheeviewDistinguish;
    private int indexCity;
    private String strDistinguish;
    private String selectCity = "";
    private boolean isSelectLocation;
    private String city;
    private HashMap<String, Object> headerMap;
    private List<Province> regionInfo;
    private int indexProvince;
    private Dialog lodingDialog;
    Boolean s1;
    Boolean s2;
    Boolean s3;


    public void getDate(Context context,Boolean s1,Boolean s2,Boolean s3) {
        this.s1=s1;
        this.s2=s2;
        this.s3=s3;

        lodingDialog = new Dialog(context, R.style.custom_dialog);
        lodingDialog.setOwnerActivity((Activity) context);
        View contentView = LayoutInflater.from(context).inflate(R.layout.item_loding, null);
        lodingDialog.setContentView(contentView);
        ImageView ivGif = contentView.findViewById(R.id.ivGif);
        Glide.with(context).load(R.drawable.loding).into(ivGif);

        ProvinceRegionDisplayAdjusted();

        list1.clear();
        for (int i = 0; i < regionInfo.size(); i++) {
            list1.add(regionInfo.get(i).province);
        }
        list2.clear();
        for (int i = 0; i < regionInfo.get(0).cities.size(); i++) {
            list2.add(regionInfo.get(0).cities.get(i).city);
        }
        if (wheeviewCity != null) {
            wheeviewCity.setAdapter(new WheelAdapter<Object>() {
                @Override
                public int getItemsCount() {
                    return list2 == null ? 0 : list2.size();
                }

                @Override
                public Object getItem(int index) {
                    return list2.get(index);
                }

                @Override
                public int indexOf(Object o) {
                    return list2.indexOf(o);
                }
            });
            // 设置默认选中第一个选项
            wheeviewCity.setCurrentItem(0);
        }
        list3.clear();
        for (int i = 0; i < regionInfo.get(0).cities.get(0).districts.size(); i++) {
            list3.add(regionInfo.get(0).cities.get(0).districts.get(i));
        }
        if (wheeviewDistinguish != null) {
            wheeviewDistinguish.setAdapter(new WheelAdapter<Object>() {
                @Override
                public int getItemsCount() {
                    return list3 == null ? 0 : list3.size();
                }

                @Override
                public Object getItem(int index) {
                    return list3.get(index);
                }

                @Override
                public int indexOf(Object o) {
                    return list3.indexOf(o);
                }
            });
            // 设置默认选中第一个选项
            wheeviewDistinguish.setCurrentItem(0);
        }


        iniPop(context);


    }

    private void iniPop(Context context) {
        Dialog adapterDialog = new Dialog(context, R.style.custom_dialog);
        adapterDialog.setOwnerActivity((Activity) context);
        View contentView = LayoutInflater.from(context).inflate(R.layout.item_location_select_dialog3, null);
        adapterDialog.setContentView(contentView);

        wheeviewProvince = contentView.findViewById(R.id.wheeviewProvince);
        wheeviewCity = contentView.findViewById(R.id.wheeviewCity);
        wheeviewDistinguish = contentView.findViewById(R.id.wheeviewDistinguish);
        LinearLayout ll1 = contentView.findViewById(R.id.ll1);
        LinearLayout ll2 = contentView.findViewById(R.id.ll2);
        LinearLayout ll3 = contentView.findViewById(R.id.ll3);
        TextView tvCancel = contentView.findViewById(R.id.tvCancel);
        TextView tvSure = contentView.findViewById(R.id.tvSure);

        if (s1==true)
            ll1.setVisibility(VISIBLE);
        if (s2==true)
            ll2.setVisibility(VISIBLE);
        if (s3==true)
            ll3.setVisibility(VISIBLE);

        tvCancel.setOnClickListener(v -> adapterDialog.dismiss());

        tvSure.setOnClickListener(v -> {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (lodingDialog != null && !lodingDialog.isShowing()) {
                        lodingDialog.show();
                    }
                }
            });
            Handler handler = new Handler(Looper.getMainLooper());
            Runnable runnable = () -> {
                int provinceIndex = wheeviewProvince.getCurrentItem();
                strProvince = wheeviewProvince.getAdapter().getItem(provinceIndex).toString();
                if (list2.size() > 0) {
                    int cityIndex = wheeviewCity.getCurrentItem();
                    strCity = wheeviewCity.getAdapter().getItem(cityIndex).toString();
                }
                int distinguishIndex = wheeviewDistinguish.getCurrentItem();
                strDistinguish = wheeviewDistinguish.getAdapter().getItem(distinguishIndex).toString();

                adapterDialog.dismiss();

                if (s1==true&&s2==true&&s3==true)
                    selectCity = strProvince + " " + strCity + " " + strDistinguish;
                if (s1==true&&s2==true&&s3==false)
                    selectCity = strProvince + " " + strCity;
                if (s1==true&&s2==false&&s3==false)
                    selectCity = strProvince;
//                selectCity = strProvince + " " + strCity;
                isSelectLocation = true;
                city = strCity;
                cityCallBack.onclick(selectCity);

                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (lodingDialog != null && lodingDialog.isShowing()) {
                            lodingDialog.dismiss();
                        }
                    }
                });
            };
            // 延迟 1000 毫秒（即 1 秒）后执行 Runnable 对象
            handler.postDelayed(runnable, 800);
        });

        wheeviewProvince.setCyclic(false);
        wheeviewCity.setCyclic(false);
        wheeviewDistinguish.setCyclic(false);


        // 设置省份适配器
        wheeviewProvince.setAdapter(new WheelAdapter<Object>() {
            @Override
            public int getItemsCount() {
                return list1 == null ? 0 : list1.size();
            }

            @Override
            public Object getItem(int index) {
                return list1.get(index);
            }

            @Override
            public int indexOf(Object o) {
                return list1.indexOf(o);
            }
        });

        // 设置城市适配器
        wheeviewCity.setAdapter(new WheelAdapter<Object>() {
            @Override
            public int getItemsCount() {
                return list2 == null ? 0 : list2.size();
            }

            @Override
            public Object getItem(int index) {
                return list2.get(index);
            }

            @Override
            public int indexOf(Object o) {
                return list2.indexOf(o);
            }
        });

        // 设置区县适配器
        wheeviewDistinguish.setAdapter(new WheelAdapter<Object>() {
            @Override
            public int getItemsCount() {
                return list3 == null ? 0 : list3.size();
            }

            @Override
            public Object getItem(int index) {
                return list3.get(index);
            }

            @Override
            public int indexOf(Object o) {
                return list3.indexOf(o);
            }
        });



        wheeviewProvince.setOnItemSelectedListener(index -> {
            indexProvince = index;
            list2.clear();
            for (int i = 0; i < regionInfo.get(indexProvince).cities.size(); i++) {
                list2.add(regionInfo.get(indexProvince).cities.get(i).city);
            }

            if (wheeviewCity != null) {
                wheeviewCity.setAdapter(new WheelAdapter<Object>() {
                    @Override
                    public int getItemsCount() {
                        return list2 == null ? 0 : list2.size();
                    }

                    @Override
                    public Object getItem(int index) {
                        return list2.get(index);
                    }

                    @Override
                    public int indexOf(Object o) {
                        return list2.indexOf(o);
                    }
                });
                // 设置默认选中第一个选项
                wheeviewCity.setCurrentItem(0);
            }

            list3.clear();
            for (int i = 0; i < regionInfo.get(indexProvince).cities.get(0).districts.size(); i++) {
                list3.add(regionInfo.get(indexProvince).cities.get(0).districts.get(i));
            }
            if (wheeviewDistinguish != null) {
                wheeviewDistinguish.setAdapter(new WheelAdapter<Object>() {
                    @Override
                    public int getItemsCount() {
                        return list3 == null ? 0 : list3.size();
                    }

                    @Override
                    public Object getItem(int index) {
                        return list3.get(index);
                    }

                    @Override
                    public int indexOf(Object o) {
                        return list3.indexOf(o);
                    }
                });
                // 设置默认选中第一个选项
                wheeviewDistinguish.setCurrentItem(0);
            }

        });

        wheeviewCity.setOnItemSelectedListener(index -> {
            indexCity = index;
            list3.clear();
            for (int i = 0; i < regionInfo.get(indexProvince).cities.get(indexCity).districts.size(); i++) {
                list3.add(regionInfo.get(indexProvince).cities.get(indexCity).districts.get(i));
            }
            if (wheeviewDistinguish != null) {
                wheeviewDistinguish.setAdapter(new WheelAdapter<Object>() {
                    @Override
                    public int getItemsCount() {
                        return list3 == null ? 0 : list3.size();
                    }
                    @Override
                    public Object getItem(int index) {
                        return list3.get(index);
                    }

                    @Override
                    public int indexOf(Object o) {
                        return list3.indexOf(o);
                    }
                });
                // 设置默认选中第一个选项
                wheeviewDistinguish.setCurrentItem(0);
            }
        });

        adapterDialog.show();
        Window window = adapterDialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.width = (int) (window.getWindowManager().getDefaultDisplay().getWidth() * 1);
            attributes.gravity = Gravity.CENTER;
            window.setAttributes(attributes);
        }
    }

    public interface CityCallBack {
        void onclick(String city);
    }

    public CityCallBack cityCallBack;

    public void setCityCallBack(CityCallBack cityCallBack) {
        this.cityCallBack = cityCallBack;
    }

    // 省级行政区类
    class Province {
        String province;
        List<City> cities;

        public Province(String province, List<City> cities) {
            this.province = province;
            this.cities = cities;
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder("省份: " + province + "\n");
            if (cities != null) {
                for (City city : cities) {
                    result.append(city.toString());
                }
            }
            return result.toString();
        }
    }

    // 城市类
    class City {
        String city;
        List<String> districts;

        public City(String city, List<String> districts) {
            this.city = city;
            this.districts = districts;
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder("  城市: " + city + "\n");
            result.append("    区县: ");
            for (String district : districts) {
                result.append(district).append(", ");
            }
            if (!districts.isEmpty()) {
                result.delete(result.length() - 2, result.length());
            }
            result.append("\n");
            return result.toString();
        }
    }

    public void ProvinceRegionDisplayAdjusted() {
        regionInfo = new ArrayList<>();
        // 北京市
        List<City> beijingCities = new ArrayList<>();
        beijingCities.add(new City("北京市", List.of("东城区", "西城区", "朝阳区", "丰台区", "石景山区", "海淀区", "门头沟区", "房山区", "通州区", "顺义区", "昌平区", "大兴区", "怀柔区", "平谷区", "密云区", "延庆区")));
        regionInfo.add(new Province("北京市", beijingCities));

        // 天津市
        List<City> tianjinCities = new ArrayList<>();
        tianjinCities.add(new City("天津市", List.of("和平区", "河东区", "河西区", "南开区", "河北区", "红桥区", "东丽区", "西青区", "津南区", "北辰区", "武清区", "宝坻区", "滨海新区", "宁河区", "静海区", "蓟州区")));
        regionInfo.add(new Province("天津市", tianjinCities));

        // 上海市
        List<City> shanghaiCities = new ArrayList<>();
        shanghaiCities.add(new City("上海市", List.of("黄浦区", "徐汇区", "长宁区", "静安区", "普陀区", "虹口区", "杨浦区", "闵行区", "宝山区", "嘉定区", "浦东新区", "金山区", "松江区", "青浦区", "奉贤区", "崇明区")));
        regionInfo.add(new Province("上海市", shanghaiCities));

        // 重庆市
        List<City> chongqingCities = new ArrayList<>();
        chongqingCities.add(new City("重庆市", List.of("万州区", "涪陵区", "渝中区", "大渡口区", "江北区", "沙坪坝区", "九龙坡区", "南岸区", "北碚区", "綦江区", "大足区", "渝北区", "巴南区", "黔江区", "长寿区", "江津区", "合川区", "永川区", "南川区", "璧山区", "铜梁区", "潼南区", "荣昌区", "开州区", "梁平区", "武隆区", "城口县", "丰都县", "垫江县", "忠县", "云阳县", "奉节县", "巫山县", "巫溪县", "石柱土家族自治县", "秀山土家族苗族自治县", "酉阳土家族苗族自治县", "彭水苗族土家族自治县")));
        regionInfo.add(new Province("重庆市", chongqingCities));

        // 河北省
        List<City> hebeiCities = new ArrayList<>();
        hebeiCities.add(new City("石家庄市", List.of("长安区", "桥西区", "新华区", "井陉矿区", "裕华区", "藁城区", "鹿泉区", "栾城区", "井陉县", "正定县", "行唐县", "灵寿县", "高邑县", "深泽县", "赞皇县", "无极县", "平山县", "元氏县", "赵县", "辛集市", "晋州市", "新乐市")));
        hebeiCities.add(new City("唐山市", List.of("路南区", "路北区", "古冶区", "开平区", "丰南区", "丰润区", "曹妃甸区", "滦南县", "乐亭县", "迁西县", "玉田县", "遵化市", "迁安市", "滦州市")));
        hebeiCities.add(new City("秦皇岛市", List.of("海港区", "山海关区", "北戴河区", "抚宁区", "青龙满族自治县", "昌黎县", "卢龙县")));
        hebeiCities.add(new City("邯郸市", List.of("邯山区", "丛台区", "复兴区", "峰峰矿区", "肥乡区", "永年区", "临漳县", "成安县", "大名县", "涉县", "磁县", "邱县", "鸡泽县", "广平县", "馆陶县", "魏县", "曲周县", "武安市")));
        hebeiCities.add(new City("邢台市", List.of("襄都区", "信都区", "任泽区", "南和区", "临城县", "内丘县", "柏乡县", "隆尧县", "宁晋县", "巨鹿县", "新河县", "广宗县", "平乡县", "威县", "清河县", "临西县", "南宫市", "沙河市")));
        hebeiCities.add(new City("保定市", List.of("竞秀区", "莲池区", "满城区", "清苑区", "徐水区", "涞水县", "阜平县", "定兴县", "唐县", "高阳县", "容城县", "涞源县", "望都县", "安新县", "易县", "曲阳县", "蠡县", "顺平县", "博野县", "雄县", "涿州市", "定州市", "安国市", "高碑店市")));
        hebeiCities.add(new City("张家口市", List.of("桥东区", "桥西区", "宣化区", "下花园区", "万全区", "崇礼区", "张北县", "康保县", "沽源县", "尚义县", "蔚县", "阳原县", "怀安县", "怀来县", "涿鹿县", "赤城县")));
        hebeiCities.add(new City("承德市", List.of("双桥区", "双滦区", "鹰手营子矿区", "承德县", "兴隆县", "滦平县", "隆化县", "丰宁满族自治县", "宽城满族自治县", "围场满族蒙古族自治县", "平泉市")));
        regionInfo.add(new Province("河北省", hebeiCities));

        // 山西省
        List<City> shanxiCities = new ArrayList<>();
        shanxiCities.add(new City("太原市", List.of("小店区", "迎泽区", "杏花岭区", "尖草坪区", "万柏林区", "晋源区", "清徐县", "阳曲县", "娄烦县", "古交市")));
        shanxiCities.add(new City("大同市", List.of("新荣区", "平城区", "云冈区", "云州区", "阳高县", "天镇县", "广灵县", "灵丘县", "浑源县", "左云县")));
        shanxiCities.add(new City("阳泉市", List.of("城区", "矿区", "郊区", "平定县", "盂县")));
        shanxiCities.add(new City("长治市", List.of("潞州区", "上党区", "屯留区", "潞城区", "襄垣县", "平顺县", "黎城县", "壶关县", "长子县", "武乡县", "沁县", "沁源县")));
        shanxiCities.add(new City("晋城市", List.of("城区", "沁水县", "阳城县", "陵川县", "泽州县", "高平市")));
        shanxiCities.add(new City("朔州市", List.of("朔城区", "平鲁区", "山阴县", "应县", "右玉县", "怀仁市")));
        shanxiCities.add(new City("晋中市", List.of("榆次区", "太谷区", "榆社县", "左权县", "和顺县", "昔阳县", "寿阳县", "祁县", "平遥县", "灵石县", "介休市")));
        shanxiCities.add(new City("运城市", List.of("盐湖区", "临猗县", "万荣县", "闻喜县", "稷山县", "新绛县", "绛县", "垣曲县", "夏县", "平陆县", "芮城县", "永济市", "河津市")));
        shanxiCities.add(new City("忻州市", List.of("忻府区", "定襄县", "五台县", "代县", "繁峙县", "宁武县", "静乐县", "神池县", "五寨县", "岢岚县", "河曲县", "保德县", "偏关县", "原平市")));
        shanxiCities.add(new City("临汾市", List.of("尧都区", "曲沃县", "翼城县", "襄汾县", "洪洞县", "古县", "安泽县", "浮山县", "吉县", "乡宁县", "大宁县", "隰县", "永和县", "蒲县", "汾西县", "侯马市", "霍州市")));
        shanxiCities.add(new City("吕梁市", List.of("离石区", "文水县", "交城县", "兴县", "临县", "柳林县", "石楼县", "岚县", "方山县", "中阳县", "交口县", "孝义市", "汾阳市")));
        regionInfo.add(new Province("山西省", shanxiCities));

// 辽宁省
        List<City> liaoningCities = new ArrayList<>();
        liaoningCities.add(new City("沈阳市", List.of("和平区", "沈河区", "大东区", "皇姑区", "铁西区", "苏家屯区", "浑南区", "沈北新区", "于洪区", "辽中区", "康平县", "法库县", "新民市")));
        liaoningCities.add(new City("大连市", List.of("中山区", "西岗区", "沙河口区", "甘井子区", "旅顺口区", "金州区", "普兰店区", "长海县", "瓦房店市", "庄河市")));
        liaoningCities.add(new City("鞍山市", List.of("铁东区", "铁西区", "立山区", "千山区", "台安县", "岫岩满族自治县", "海城市")));
        liaoningCities.add(new City("抚顺市", List.of("新抚区", "东洲区", "望花区", "顺城区", "抚顺县", "新宾满族自治县", "清原满族自治县")));
        liaoningCities.add(new City("本溪市", List.of("平山区", "溪湖区", "明山区", "南芬区", "本溪满族自治县", "桓仁满族自治县")));
        liaoningCities.add(new City("丹东市", List.of("元宝区", "振兴区", "振安区", "宽甸满族自治县", "东港市", "凤城市")));
        liaoningCities.add(new City("锦州市", List.of("古塔区", "凌河区", "太和区", "黑山县", "义县", "凌海市", "北镇市")));
        liaoningCities.add(new City("营口市", List.of("站前区", "西市区", "鲅鱼圈区", "老边区", "盖州市", "大石桥市")));
        liaoningCities.add(new City("阜新市", List.of("海州区", "新邱区", "太平区", "清河门区", "细河区", "阜新蒙古族自治县", "彰武县")));
        liaoningCities.add(new City("辽阳市", List.of("白塔区", "文圣区", "宏伟区", "弓长岭区", "太子河区", "辽阳县", "灯塔市")));
        liaoningCities.add(new City("盘锦市", List.of("双台子区", "兴隆台区", "大洼区", "盘山县")));
        liaoningCities.add(new City("铁岭市", List.of("银州区", "清河区", "铁岭县", "西丰县", "昌图县", "调兵山市", "开原市")));
        liaoningCities.add(new City("朝阳市", List.of("双塔区", "龙城区", "朝阳县", "建平县", "喀喇沁左翼蒙古族自治县", "北票市", "凌源市")));
        liaoningCities.add(new City("葫芦岛市", List.of("连山区", "龙港区", "南票区", "绥中县", "建昌县", "兴城市")));
        regionInfo.add(new Province("辽宁省", liaoningCities));

// 吉林省
        List<City> jilinCities = new ArrayList<>();
        jilinCities.add(new City("长春市", List.of("南关区", "宽城区", "朝阳区", "二道区", "绿园区", "双阳区", "九台区", "农安县", "榆树市", "德惠市", "公主岭市")));
        jilinCities.add(new City("吉林市", List.of("昌邑区", "龙潭区", "船营区", "丰满区", "永吉县", "蛟河市", "桦甸市", "舒兰市", "磐石市")));
        jilinCities.add(new City("四平市", List.of("铁西区", "铁东区", "梨树县", "伊通满族自治县", "双辽市")));
        jilinCities.add(new City("辽源市", List.of("龙山区", "西安区", "东丰县", "东辽县")));
        jilinCities.add(new City("通化市", List.of("东昌区", "二道江区", "通化县", "辉南县", "柳河县", "梅河口市", "集安市")));
        jilinCities.add(new City("白山市", List.of("浑江区", "江源区", "抚松县", "靖宇县", "长白朝鲜族自治县", "临江市")));
        jilinCities.add(new City("松原市", List.of("宁江区", "前郭尔罗斯蒙古族自治县", "长岭县", "乾安县", "扶余市")));
        jilinCities.add(new City("白城市", List.of("洮北区", "镇赉县", "通榆县", "洮南市", "大安市")));
        jilinCities.add(new City("延边朝鲜族自治州", List.of("延吉市", "图们市", "敦化市", "珲春市", "龙井市", "和龙市", "汪清县", "安图县")));
        regionInfo.add(new Province("吉林省", jilinCities));

// 黑龙江省
        List<City> heilongjiangCities = new ArrayList<>();
        heilongjiangCities.add(new City("哈尔滨市", List.of("道里区", "南岗区", "道外区", "平房区", "松北区", "香坊区", "呼兰区", "阿城区", "双城区", "依兰县", "方正县", "宾县", "巴彦县", "木兰县", "通河县", "延寿县", "尚志市", "五常市")));
        heilongjiangCities.add(new City("齐齐哈尔市", List.of("龙沙区", "建华区", "铁锋区", "昂昂溪区", "富拉尔基区", "碾子山区", "梅里斯达斡尔族区", "龙江县", "依安县", "泰来县", "甘南县", "富裕县", "克山县", "克东县", "拜泉县", "讷河市")));
        heilongjiangCities.add(new City("鸡西市", List.of("鸡冠区", "恒山区", "滴道区", "梨树区", "城子河区", "麻山区", "鸡东县", "虎林市", "密山市")));
        heilongjiangCities.add(new City("鹤岗市", List.of("向阳区", "工农区", "南山区", "兴安区", "东山区", "兴山区", "萝北县", "绥滨县")));
        heilongjiangCities.add(new City("双鸭山市", List.of("尖山区", "岭东区", "四方台区", "宝山区", "集贤县", "友谊县", "宝清县", "饶河县")));
        heilongjiangCities.add(new City("大庆市", List.of("萨尔图区", "龙凤区", "让胡路区", "红岗区", "大同区", "肇州县", "肇源县", "林甸县", "杜尔伯特蒙古族自治县")));
        heilongjiangCities.add(new City("伊春市", List.of("伊美区", "乌翠区", "友好区", "金林区", "汤旺县", "丰林县", "大箐山县", "南岔县", "嘉荫县", "铁力市")));
        heilongjiangCities.add(new City("佳木斯市", List.of("向阳区", "前进区", "东风区", "郊区", "桦南县", "桦川县", "汤原县", "抚远市", "同江市", "富锦市")));
        heilongjiangCities.add(new City("七台河市", List.of("新兴区", "桃山区", "茄子河区", "勃利县")));
        heilongjiangCities.add(new City("牡丹江市", List.of("东安区", "阳明区", "爱民区", "西安区", "林口县", "绥芬河市", "海林市", "宁安市", "穆棱市", "东宁市")));
        heilongjiangCities.add(new City("黑河市", List.of("爱辉区", "嫩江市", "逊克县", "孙吴县", "北安市", "五大连池市")));
        heilongjiangCities.add(new City("绥化市", List.of("北林区", "望奎县", "兰西县", "青冈县", "庆安县", "明水县", "绥棱县", "安达市", "肇东市", "海伦市")));
        heilongjiangCities.add(new City("大兴安岭地区", List.of("加格达奇区", "松岭区", "新林区", "呼中区", "呼玛县", "塔河县", "漠河市")));
        regionInfo.add(new Province("黑龙江省", heilongjiangCities));

// 江苏省
        List<City> jiangsuCities = new ArrayList<>();
        jiangsuCities.add(new City("南京市", List.of("玄武区", "秦淮区", "建邺区", "鼓楼区", "浦口区", "栖霞区", "雨花台区", "江宁区", "六合区", "溧水区", "高淳区")));
        jiangsuCities.add(new City("无锡市", List.of("锡山区", "惠山区", "滨湖区", "梁溪区", "新吴区", "江阴市", "宜兴市")));
        jiangsuCities.add(new City("徐州市", List.of("鼓楼区", "云龙区", "贾汪区", "泉山区", "铜山区", "丰县", "沛县", "睢宁县", "新沂市", "邳州市")));
        jiangsuCities.add(new City("常州市", List.of("天宁区", "钟楼区", "新北区", "武进区", "金坛区", "溧阳市")));
        jiangsuCities.add(new City("苏州市", List.of("虎丘区", "吴中区", "相城区", "姑苏区", "吴江区", "常熟市", "张家港市", "昆山市", "太仓市")));
        jiangsuCities.add(new City("南通市", List.of("崇川区", "通州区", "海门区", "如东县", "启东市", "如皋市", "海安市")));
        jiangsuCities.add(new City("连云港市", List.of("连云区", "海州区", "赣榆区", "东海县", "灌云县", "灌南县")));
        jiangsuCities.add(new City("淮安市", List.of("清江浦区", "淮阴区", "淮安区", "洪泽区", "涟水县", "盱眙县", "金湖县")));
        jiangsuCities.add(new City("盐城市", List.of("亭湖区", "盐都区", "大丰区", "响水县", "滨海县", "阜宁县", "射阳县", "建湖县", "东台市")));
        jiangsuCities.add(new City("扬州市", List.of("广陵区", "邗江区", "江都区", "宝应县", "仪征市", "高邮市")));
        jiangsuCities.add(new City("镇江市", List.of("京口区", "润州区", "丹徒区", "丹阳市", "扬中市", "句容市")));
        jiangsuCities.add(new City("泰州市", List.of("海陵区", "高港区", "姜堰区", "兴化市", "靖江市", "泰兴市")));
        jiangsuCities.add(new City("宿迁市", List.of("宿城区", "宿豫区", "沭阳县", "泗阳县", "泗洪县")));
        regionInfo.add(new Province("江苏省", jiangsuCities));
// 浙江省
        List<City> zhejiangCities = new ArrayList<>();
        zhejiangCities.add(new City("杭州市", List.of("上城区", "拱墅区", "西湖区", "滨江区", "萧山区", "余杭区", "临平区", "钱塘区", "富阳区", "临安区", "桐庐县", "淳安县", "建德市")));
        zhejiangCities.add(new City("宁波市", List.of("海曙区", "江北区", "北仑区", "镇海区", "鄞州区", "奉化区", "象山县", "宁海县", "余姚市", "慈溪市")));
        zhejiangCities.add(new City("温州市", List.of("鹿城区", "龙湾区", "瓯海区", "洞头区", "永嘉县", "平阳县", "苍南县", "文成县", "泰顺县", "瑞安市", "乐清市", "龙港市")));
        zhejiangCities.add(new City("嘉兴市", List.of("南湖区", "秀洲区", "嘉善县", "海盐县", "海宁市", "平湖市", "桐乡市")));
        zhejiangCities.add(new City("湖州市", List.of("吴兴区", "南浔区", "德清县", "长兴县", "安吉县")));
        zhejiangCities.add(new City("绍兴市", List.of("越城区", "柯桥区", "上虞区", "新昌县", "诸暨市", "嵊州市")));
        zhejiangCities.add(new City("金华市", List.of("婺城区", "金东区", "武义县", "浦江县", "磐安县", "兰溪市", "义乌市", "东阳市", "永康市")));
        zhejiangCities.add(new City("衢州市", List.of("柯城区", "衢江区", "常山县", "开化县", "龙游县", "江山市")));
        zhejiangCities.add(new City("舟山市", List.of("定海区", "普陀区", "岱山县", "嵊泗县")));
        zhejiangCities.add(new City("台州市", List.of("椒江区", "黄岩区", "路桥区", "三门县", "天台县", "仙居县", "温岭市", "临海市", "玉环市")));
        zhejiangCities.add(new City("丽水市", List.of("莲都区", "青田县", "缙云县", "遂昌县", "松阳县", "云和县", "庆元县", "景宁畲族自治县", "龙泉市")));
        regionInfo.add(new Province("浙江省", zhejiangCities));

// 安徽省
        List<City> anhuiCities = new ArrayList<>();
        anhuiCities.add(new City("合肥市", List.of("瑶海区", "庐阳区", "蜀山区", "包河区", "长丰县", "肥东县", "肥西县", "庐江县", "巢湖市")));
        anhuiCities.add(new City("芜湖市", List.of("镜湖区", "弋江区", "鸠江区", "湾沚区", "繁昌区", "南陵县", "无为市")));
        anhuiCities.add(new City("蚌埠市", List.of("龙子湖区", "蚌山区", "禹会区", "淮上区", "怀远县", "五河县", "固镇县")));
        anhuiCities.add(new City("淮南市", List.of("大通区", "田家庵区", "谢家集区", "八公山区", "潘集区", "凤台县", "寿县")));
        anhuiCities.add(new City("马鞍山市", List.of("花山区", "雨山区", "博望区", "当涂县", "含山县", "和县")));
        anhuiCities.add(new City("淮北市", List.of("杜集区", "相山区", "烈山区", "濉溪县")));
        anhuiCities.add(new City("铜陵市", List.of("铜官区", "义安区", "郊区", "枞阳县")));
        anhuiCities.add(new City("安庆市", List.of("迎江区", "大观区", "宜秀区", "怀宁县", "太湖县", "宿松县", "望江县", "岳西县", "桐城市", "潜山市")));
        anhuiCities.add(new City("黄山市", List.of("屯溪区", "黄山区", "徽州区", "歙县", "休宁县", "黟县", "祁门县")));
        anhuiCities.add(new City("滁州市", List.of("琅琊区", "南谯区", "来安县", "全椒县", "定远县", "凤阳县", "天长市", "明光市")));
        anhuiCities.add(new City("阜阳市", List.of("颍州区", "颍东区", "颍泉区", "临泉县", "太和县", "阜南县", "颍上县", "界首市")));
        anhuiCities.add(new City("宿州市", List.of("埇桥区", "砀山县", "萧县", "灵璧县", "泗县")));
        anhuiCities.add(new City("六安市", List.of("金安区", "裕安区", "叶集区", "霍邱县", "舒城县", "金寨县", "霍山县")));
        anhuiCities.add(new City("亳州市", List.of("谯城区", "涡阳县", "蒙城县", "利辛县")));
        anhuiCities.add(new City("池州市", List.of("贵池区", "东至县", "石台县", "青阳县")));
        anhuiCities.add(new City("宣城市", List.of("宣州区", "郎溪县", "广德市", "泾县", "绩溪县", "旌德县", "宁国市")));
        regionInfo.add(new Province("安徽省", anhuiCities));

// 福建省
        List<City> fujianCities = new ArrayList<>();
        fujianCities.add(new City("福州市", List.of("鼓楼区", "台江区", "仓山区", "马尾区", "晋安区", "长乐区", "闽侯县", "连江县", "罗源县", "闽清县", "永泰县", "平潭县", "福清市")));
        fujianCities.add(new City("厦门市", List.of("思明区", "湖里区", "集美区", "海沧区", "同安区", "翔安区")));
        fujianCities.add(new City("莆田市", List.of("城厢区", "涵江区", "荔城区", "秀屿区", "仙游县")));
        fujianCities.add(new City("三明市", List.of("梅列区", "三元区", "明溪县", "清流县", "宁化县", "大田县", "尤溪县", "沙县", "将乐县", "泰宁县", "建宁县", "永安市")));
        fujianCities.add(new City("泉州市", List.of("鲤城区", "丰泽区", "洛江区", "泉港区", "惠安县", "安溪县", "永春县", "德化县", "金门县", "石狮市", "晋江市", "南安市")));
        fujianCities.add(new City("漳州市", List.of("芗城区", "龙文区", "龙海区", "长泰区", "云霄县", "漳浦县", "诏安县", "东山县", "南靖县", "平和县", "华安县")));
        fujianCities.add(new City("南平市", List.of("延平区", "建阳区", "顺昌县", "浦城县", "光泽县", "松溪县", "政和县", "邵武市", "武夷山市", "建瓯市")));
        fujianCities.add(new City("龙岩市", List.of("新罗区", "永定区", "长汀县", "上杭县", "武平县", "连城县", "漳平市")));
        fujianCities.add(new City("宁德市", List.of("蕉城区", "霞浦县", "古田县", "屏南县", "寿宁县", "周宁县", "柘荣县", "福安市", "福鼎市")));
        regionInfo.add(new Province("福建省", fujianCities));

// 江西省
        List<City> jiangxiCities = new ArrayList<>();
        jiangxiCities.add(new City("南昌市", List.of("东湖区", "西湖区", "青云谱区", "湾里区", "青山湖区", "新建区", "南昌县", "安义县", "进贤县")));
        jiangxiCities.add(new City("景德镇市", List.of("昌江区", "珠山区", "浮梁县", "乐平市")));
        jiangxiCities.add(new City("萍乡市", List.of("安源区", "湘东区", "莲花县", "上栗县", "芦溪县")));
        jiangxiCities.add(new City("九江市", List.of("濂溪区", "浔阳区", "柴桑区", "武宁县", "修水县", "永修县", "德安县", "都昌县", "湖口县", "彭泽县", "瑞昌市", "共青城市", "庐山市")));
        jiangxiCities.add(new City("新余市", List.of("渝水区", "分宜县")));
        jiangxiCities.add(new City("鹰潭市", List.of("月湖区", "余江区", "贵溪市")));
        jiangxiCities.add(new City("赣州市", List.of("章贡区", "南康区", "赣县区", "信丰县", "大余县", "上犹县", "崇义县", "安远县", "定南县", "全南县", "宁都县", "于都县", "兴国县", "会昌县", "寻乌县", "石城县", "瑞金市", "龙南市")));
        jiangxiCities.add(new City("吉安市", List.of("吉州区", "青原区", "吉安县", "吉水县", "峡江县", "新干县", "永丰县", "泰和县", "遂川县", "万安县", "安福县", "永新县", "井冈山市")));
        jiangxiCities.add(new City("宜春市", List.of("袁州区", "奉新县", "万载县", "上高县", "宜丰县", "靖安县", "铜鼓县", "丰城市", "樟树市", "高安市")));
        jiangxiCities.add(new City("抚州市", List.of("临川区", "东乡区", "南城县", "黎川县", "南丰县", "崇仁县", "乐安县", "宜黄县", "金溪县", "资溪县", "广昌县")));
        jiangxiCities.add(new City("上饶市", List.of("信州区", "广丰区", "广信区", "玉山县", "铅山县", "横峰县", "弋阳县", "余干县", "鄱阳县", "万年县", "婺源县", "德兴市")));
        regionInfo.add(new Province("江西省", jiangxiCities));

// 山东省
        List<City> shandongCities = new ArrayList<>();
        shandongCities.add(new City("济南市", List.of("历下区", "市中区", "槐荫区", "天桥区", "历城区", "长清区", "章丘区", "济阳区", "莱芜区", "钢城区", "平阴县", "商河县")));
        shandongCities.add(new City("青岛市", List.of("市南区", "市北区", "黄岛区", "崂山区", "李沧区", "城阳区", "即墨区", "胶州市", "平度市", "莱西市")));
        shandongCities.add(new City("淄博市", List.of("淄川区", "张店区", "博山区", "临淄区", "周村区", "桓台县", "高青县", "沂源县")));
        shandongCities.add(new City("枣庄市", List.of("市中区", "薛城区", "峄城区", "台儿庄区", "山亭区", "滕州市")));
        shandongCities.add(new City("东营市", List.of("东营区", "河口区", "垦利区", "利津县", "广饶县")));
        shandongCities.add(new City("烟台市", List.of("芝罘区", "福山区", "牟平区", "莱山区", "蓬莱区", "长岛县", "龙口市", "莱阳市", "莱州市", "招远市", "栖霞市", "海阳市")));
        shandongCities.add(new City("潍坊市", List.of("潍城区", "寒亭区", "坊子区", "奎文区", "临朐县", "昌乐县", "青州市", "诸城市", "寿光市", "安丘市", "高密市", "昌邑市")));
        shandongCities.add(new City("济宁市", List.of("任城区", "兖州区", "微山县", "鱼台县", "金乡县", "嘉祥县", "汶上县", "泗水县", "梁山县", "曲阜市", "邹城市")));
        shandongCities.add(new City("泰安市", List.of("泰山区", "岱岳区", "宁阳县", "东平县", "新泰市", "肥城市")));
        shandongCities.add(new City("威海市", List.of("环翠区", "文登区", "荣成市", "乳山市")));
        shandongCities.add(new City("日照市", List.of("东港区", "岚山区", "五莲县", "莒县")));
        shandongCities.add(new City("临沂市", List.of("兰山区", "罗庄区", "河东区", "沂南县", "郯城县", "沂水县", "兰陵县", "费县", "平邑县", "莒南县", "蒙阴县", "临沭县")));
        shandongCities.add(new City("德州市", List.of("德城区", "陵城区", "宁津县", "庆云县", "临邑县", "齐河县", "平原县", "夏津县", "武城县", "乐陵市", "禹城市")));
        shandongCities.add(new City("聊城市", List.of("东昌府区", "茌平区", "阳谷县", "莘县", "东阿县", "冠县", "高唐县", "临清市")));
        shandongCities.add(new City("滨州市", List.of("滨城区", "沾化区", "惠民县", "阳信县", "无棣县", "博兴县", "邹平市")));
        shandongCities.add(new City("菏泽市", List.of("牡丹区", "定陶区", "曹县", "单县", "成武县", "巨野县", "郓城县", "鄄城县", "东明县")));
        regionInfo.add(new Province("山东省", shandongCities));

        // 河南省
        List<City> henanCities = new ArrayList<>();
        henanCities.add(new City("郑州市", List.of("中原区", "二七区", "管城回族区", "金水区", "上街区", "惠济区", "中牟县", "巩义市", "荥阳市", "新密市", "新郑市", "登封市")));
        henanCities.add(new City("开封市", List.of("龙亭区", "顺河回族区", "鼓楼区", "禹王台区", "祥符区", "杞县", "通许县", "尉氏县", "兰考县")));
        henanCities.add(new City("洛阳市", List.of("老城区", "西工区", "瀍河回族区", "涧西区", "吉利区", "洛龙区", "孟津县", "新安县", "栾川县", "嵩县", "汝阳县", "宜阳县", "洛宁县", "伊川县", "偃师市")));
        henanCities.add(new City("平顶山市", List.of("新华区", "卫东区", "石龙区", "湛河区", "宝丰县", "叶县", "鲁山县", "郏县", "舞钢市", "汝州市")));
        henanCities.add(new City("安阳市", List.of("文峰区", "北关区", "殷都区", "龙安区", "安阳县", "汤阴县", "滑县", "内黄县", "林州市")));
        henanCities.add(new City("鹤壁市", List.of("鹤山区", "山城区", "淇滨区", "浚县", "淇县")));
        henanCities.add(new City("新乡市", List.of("红旗区", "卫滨区", "凤泉区", "牧野区", "新乡县", "获嘉县", "原阳县", "延津县", "封丘县", "长垣市", "卫辉市", "辉县市")));
        henanCities.add(new City("焦作市", List.of("解放区", "中站区", "马村区", "山阳区", "修武县", "博爱县", "武陟县", "温县", "沁阳市", "孟州市")));
        henanCities.add(new City("濮阳市", List.of("华龙区", "清丰县", "南乐县", "范县", "台前县", "濮阳县")));
        henanCities.add(new City("许昌市", List.of("魏都区", "建安区", "鄢陵县", "襄城县", "禹州市", "长葛市")));
        henanCities.add(new City("漯河市", List.of("源汇区", "郾城区", "召陵区", "舞阳县", "临颍县")));
        henanCities.add(new City("三门峡市", List.of("湖滨区", "陕州区", "渑池县", "卢氏县", "义马市", "灵宝市")));
        henanCities.add(new City("南阳市", List.of("宛城区", "卧龙区", "南召县", "方城县", "西峡县", "镇平县", "内乡县", "淅川县", "社旗县", "唐河县", "新野县", "桐柏县", "邓州市")));
        henanCities.add(new City("商丘市", List.of("梁园区", "睢阳区", "民权县", "睢县", "宁陵县", "柘城县", "虞城县", "夏邑县", "永城市")));
        henanCities.add(new City("信阳市", List.of("浉河区", "平桥区", "罗山县", "光山县", "新县", "商城县", "固始县", "潢川县", "淮滨县", "息县")));
        henanCities.add(new City("周口市", List.of("川汇区", "淮阳区", "扶沟县", "西华县", "商水县", "沈丘县", "郸城县", "太康县", "鹿邑县", "项城市")));
        henanCities.add(new City("驻马店市", List.of("驿城区", "西平县", "上蔡县", "平舆县", "正阳县", "确山县", "泌阳县", "汝南县", "遂平县", "新蔡县")));
        henanCities.add(new City("济源市", List.of()));
        regionInfo.add(new Province("河南省", henanCities));

// 湖北省
        List<City> hubeiCities = new ArrayList<>();
        hubeiCities.add(new City("武汉市", List.of("江岸区", "江汉区", "硚口区", "汉阳区", "武昌区", "青山区", "洪山区", "东西湖区", "汉南区", "蔡甸区", "江夏区", "黄陂区", "新洲区")));
        hubeiCities.add(new City("黄石市", List.of("黄石港区", "西塞山区", "下陆区", "铁山区", "阳新县", "大冶市")));
        hubeiCities.add(new City("十堰市", List.of("茅箭区", "张湾区", "郧阳区", "郧西县", "竹山县", "竹溪县", "房县", "丹江口市")));
        hubeiCities.add(new City("宜昌市", List.of("西陵区", "伍家岗区", "点军区", "猇亭区", "夷陵区", "远安县", "兴山县", "秭归县", "长阳土家族自治县", "五峰土家族自治县", "宜都市", "当阳市", "枝江市")));
        hubeiCities.add(new City("襄阳市", List.of("襄城区", "樊城区", "襄州区", "南漳县", "谷城县", "保康县", "老河口市", "枣阳市", "宜城市")));
        hubeiCities.add(new City("鄂州市", List.of("梁子湖区", "华容区", "鄂城区")));
        hubeiCities.add(new City("荆门市", List.of("东宝区", "掇刀区", "沙洋县", "钟祥市", "京山市")));
        hubeiCities.add(new City("孝感市", List.of("孝南区", "孝昌县", "大悟县", "云梦县", "应城市", "安陆市", "汉川市")));
        hubeiCities.add(new City("荆州市", List.of("沙市区", "荆州区", "公安县", "监利市", "江陵县", "石首市", "洪湖市", "松滋市")));
        hubeiCities.add(new City("黄冈市", List.of("黄州区", "团风县", "红安县", "罗田县", "英山县", "浠水县", "蕲春县", "黄梅县", "麻城市", "武穴市")));
        hubeiCities.add(new City("咸宁市", List.of("咸安区", "嘉鱼县", "通城县", "崇阳县", "通山县", "赤壁市")));
        hubeiCities.add(new City("随州市", List.of("曾都区", "随县", "广水市")));
        hubeiCities.add(new City("恩施土家族苗族自治州", List.of("恩施市", "利川市", "建始县", "巴东县", "宣恩县", "咸丰县", "来凤县", "鹤峰县")));
        hubeiCities.add(new City("仙桃市", List.of("仙桃市")));
        hubeiCities.add(new City("潜江市", List.of("潜江市")));
        hubeiCities.add(new City("天门市", List.of("天门市")));
        hubeiCities.add(new City("神农架林区", List.of("神农架林区")));
        regionInfo.add(new Province("湖北省", hubeiCities));

// 湖南省
        List<City> hunanCities = new ArrayList<>();
        hunanCities.add(new City("长沙市", List.of("芙蓉区", "天心区", "岳麓区", "开福区", "雨花区", "望城区", "长沙县", "宁乡市", "浏阳市")));
        hunanCities.add(new City("株洲市", List.of("荷塘区", "芦淞区", "石峰区", "天元区", "渌口区", "攸县", "茶陵县", "炎陵县", "醴陵市")));
        hunanCities.add(new City("湘潭市", List.of("雨湖区", "岳塘区", "湘潭县", "湘乡市", "韶山市")));
        hunanCities.add(new City("衡阳市", List.of("珠晖区", "雁峰区", "石鼓区", "蒸湘区", "南岳区", "衡阳县", "衡南县", "衡山县", "衡东县", "祁东县", "耒阳市", "常宁市")));
        hunanCities.add(new City("邵阳市", List.of("双清区", "大祥区", "北塔区", "邵东市", "新邵县", "邵阳县", "隆回县", "洞口县", "绥宁县", "新宁县", "城步苗族自治县", "武冈市")));
        hunanCities.add(new City("岳阳市", List.of("岳阳楼区", "云溪区", "君山区", "岳阳县", "华容县", "湘阴县", "平江县", "汨罗市", "临湘市")));
        hunanCities.add(new City("常德市", List.of("武陵区", "鼎城区", "安乡县", "汉寿县", "澧县", "临澧县", "桃源县", "石门县", "津市市")));
        hunanCities.add(new City("张家界市", List.of("永定区", "武陵源区", "慈利县", "桑植县")));
        hunanCities.add(new City("益阳市", List.of("资阳区", "赫山区", "南县", "桃江县", "安化县", "沅江市")));
        hunanCities.add(new City("郴州市", List.of("北湖区", "苏仙区", "桂阳县", "宜章县", "永兴县", "嘉禾县", "临武县", "汝城县", "桂东县", "安仁县", "资兴市")));
        hunanCities.add(new City("永州市", List.of("零陵区", "冷水滩区", "祁阳市", "东安县", "双牌县", "道县", "江永县", "宁远县", "蓝山县", "新田县", "江华瑶族自治县")));
        hunanCities.add(new City("怀化市", List.of("鹤城区", "中方县", "沅陵县", "辰溪县", "溆浦县", "会同县", "麻阳苗族自治县", "新晃侗族自治县", "芷江侗族自治县", "靖州苗族侗族自治县", "通道侗族自治县", "洪江市")));
        hunanCities.add(new City("娄底市", List.of("娄星区", "双峰县", "新化县", "冷水江市", "涟源市")));
        hunanCities.add(new City("湘西土家族苗族自治州", List.of("吉首市", "泸溪县", "凤凰县", "花垣县", "保靖县", "古丈县", "永顺县", "龙山县")));
        regionInfo.add(new Province("湖南省", hunanCities));

// 广东省
        List<City> guangdongCities = new ArrayList<>();
        guangdongCities.add(new City("广州市", List.of("荔湾区", "越秀区", "海珠区", "天河区", "白云区", "黄埔区", "番禺区", "花都区", "南沙区", "从化区", "增城区")));
        guangdongCities.add(new City("韶关市", List.of("武江区", "浈江区", "曲江区", "始兴县", "仁化县", "翁源县", "乳源瑶族自治县", "新丰县", "乐昌市", "南雄市")));
        guangdongCities.add(new City("深圳市", List.of("罗湖区", "福田区", "南山区", "宝安区", "龙岗区", "盐田区", "龙华区", "坪山区", "光明区")));
        guangdongCities.add(new City("珠海市", List.of("香洲区", "斗门区", "金湾区")));
        guangdongCities.add(new City("汕头市", List.of("龙湖区", "金平区", "濠江区", "潮阳区", "潮南区", "澄海区", "南澳县")));
        guangdongCities.add(new City("佛山市", List.of("禅城区", "南海区", "顺德区", "三水区", "高明区")));
        guangdongCities.add(new City("江门市", List.of("蓬江区", "江海区", "新会区", "台山市", "开平市", "鹤山市", "恩平市")));
        guangdongCities.add(new City("湛江市", List.of("赤坎区", "霞山区", "坡头区", "麻章区", "遂溪县", "徐闻县", "廉江市", "雷州市", "吴川市")));
        guangdongCities.add(new City("茂名市", List.of("茂南区", "电白区", "高州市", "化州市", "信宜市")));
        guangdongCities.add(new City("肇庆市", List.of("端州区", "鼎湖区", "高要区", "广宁县", "怀集县", "封开县", "德庆县", "四会市")));
        guangdongCities.add(new City("惠州市", List.of("惠城区", "惠阳区", "博罗县", "惠东县", "龙门县")));
        guangdongCities.add(new City("梅州市", List.of("梅江区", "梅县区", "大埔县", "丰顺县", "五华县", "平远县", "蕉岭县", "兴宁市")));
        guangdongCities.add(new City("汕尾市", List.of("城区", "海丰县", "陆河县", "陆丰市")));
        guangdongCities.add(new City("河源市", List.of("源城区", "紫金县", "龙川县", "连平县", "和平县", "东源县")));
        guangdongCities.add(new City("阳江市", List.of("江城区", "阳西县", "阳东区", "阳春市")));
        guangdongCities.add(new City("清远市", List.of("清城区", "清新区", "佛冈县", "阳山县", "连山壮族瑶族自治县", "连南瑶族自治县", "英德市", "连州市")));
        guangdongCities.add(new City("东莞市", List.of("东莞市")));
        guangdongCities.add(new City("中山市", List.of("中山市")));
        guangdongCities.add(new City("潮州市", List.of("湘桥区", "潮安区", "饶平县")));
        guangdongCities.add(new City("揭阳市", List.of("榕城区", "揭东区", "揭西县", "惠来县", "普宁市")));
        guangdongCities.add(new City("云浮市", List.of("云城区", "云安区", "新兴县", "郁南县", "罗定市")));
        regionInfo.add(new Province("广东省", guangdongCities));

// 海南省
        List<City> hainanCities = new ArrayList<>();
        hainanCities.add(new City("海口市", List.of("秀英区", "龙华区", "琼山区", "美兰区")));
        hainanCities.add(new City("三亚市", List.of("海棠区", "吉阳区", "天涯区", "崖州区")));
        hainanCities.add(new City("三沙市", List.of("三沙市")));
        hainanCities.add(new City("儋州市", List.of("儋州市")));
        hainanCities.add(new City("五指山市", List.of("五指山市")));
        hainanCities.add(new City("琼海市", List.of("琼海市")));
        hainanCities.add(new City("文昌市", List.of("文昌市")));
        hainanCities.add(new City("万宁市", List.of("万宁市")));
        hainanCities.add(new City("东方市", List.of("东方市")));
        hainanCities.add(new City("定安县", List.of("定安县")));
        hainanCities.add(new City("屯昌县", List.of("屯昌县")));
        hainanCities.add(new City("澄迈县", List.of("澄迈县")));
        hainanCities.add(new City("临高县", List.of("临高县")));
        hainanCities.add(new City("白沙黎族自治县", List.of("白沙黎族自治县")));
        hainanCities.add(new City("昌江黎族自治县", List.of("昌江黎族自治县")));
        hainanCities.add(new City("昌江黎族自治县", List.of("昌江黎族自治县")));
        hainanCities.add(new City("陵水黎族自治县", List.of("陵水黎族自治县")));
        hainanCities.add(new City("保亭黎族苗族自治县", List.of("保亭黎族苗族自治县")));
        hainanCities.add(new City("琼中黎族苗族自治县", List.of("琼中黎族苗族自治县")));
        regionInfo.add(new Province("海南省", hainanCities));

// 四川省
        List<City> sichuanCities = new ArrayList<>();
        sichuanCities.add(new City("成都市", List.of("锦江区", "青羊区", "金牛区", "武侯区", "成华区", "龙泉驿区", "青白江区", "新都区", "温江区", "双流区", "郫都区", "金堂县", "大邑县", "蒲江县", "新津县", "都江堰市", "彭州市", "邛崃市", "崇州市", "简阳市")));
        sichuanCities.add(new City("自贡市", List.of("自流井区", "贡井区", "大安区", "沿滩区", "荣县", "富顺县")));
        sichuanCities.add(new City("攀枝花市", List.of("东区", "西区", "仁和区", "米易县", "盐边县")));
        sichuanCities.add(new City("泸州市", List.of("江阳区", "纳溪区", "龙马潭区", "泸县", "合江县", "叙永县", "古蔺县")));
        sichuanCities.add(new City("德阳市", List.of("旌阳区", "罗江区", "中江县", "广汉市", "什邡市", "绵竹市")));
        sichuanCities.add(new City("绵阳市", List.of("涪城区", "游仙区", "安州区", "三台县", "盐亭县", "梓潼县", "平武县", "北川羌族自治县", "江油市")));
        sichuanCities.add(new City("广元市", List.of("利州区", "昭化区", "朝天区", "旺苍县", "青川县", "剑阁县", "苍溪县")));
        sichuanCities.add(new City("遂宁市", List.of("船山区", "安居区", "蓬溪县", "射洪市", "大英县")));
        sichuanCities.add(new City("内江市", List.of("市中区", "东兴区", "威远县", "资中县", "隆昌市")));
        sichuanCities.add(new City("乐山市", List.of("市中区", "沙湾区", "五通桥区", "金口河区", "犍为县", "井研县", "夹江县", "沐川县", "峨边彝族自治县", "马边彝族自治县", "峨眉山市")));
        sichuanCities.add(new City("南充市", List.of("顺庆区", "高坪区", "嘉陵区", "南部县", "营山县", "蓬安县", "仪陇县", "西充县", "阆中市")));
        sichuanCities.add(new City("眉山市", List.of("东坡区", "彭山区", "仁寿县", "洪雅县", "丹棱县", "青神县")));
        sichuanCities.add(new City("宜宾市", List.of("翠屏区", "南溪区", "叙州区", "江安县", "长宁县", "高县", "珙县", "筠连县", "兴文县", "屏山县")));
        sichuanCities.add(new City("广安市", List.of("广安区", "前锋区", "岳池县", "武胜县", "邻水县", "华蓥市")));
        sichuanCities.add(new City("达州市", List.of("通川区", "达川区", "宣汉县", "开江县", "大竹县", "渠县", "万源市")));
        sichuanCities.add(new City("雅安市", List.of("雨城区", "名山区", "荥经县", "汉源县", "石棉县", "天全县", "芦山县", "宝兴县")));
        sichuanCities.add(new City("巴中市", List.of("巴州区", "恩阳区", "平昌县", "通江县", "南江县")));
        sichuanCities.add(new City("资阳市", List.of("雁江区", "安岳县", "乐至县")));
        sichuanCities.add(new City("阿坝藏族羌族自治州", List.of("马尔康市", "汶川县", "理县", "茂县", "松潘县", "九寨沟县", "金川县", "小金县", "黑水县", "壤塘县", "阿坝县", "若尔盖县", "红原县")));
        sichuanCities.add(new City("甘孜藏族自治州", List.of("康定市", "泸定县", "丹巴县", "九龙县", "雅江县", "道孚县", "炉霍县", "甘孜县", "新龙县", "德格县", "白玉县", "石渠县", "色达县", "理塘县", "巴塘县", "乡城县", "稻城县", "得荣县")));
        sichuanCities.add(new City("凉山彝族自治州", List.of("西昌市", "木里藏族自治县", "盐源县", "德昌县", "会理市", "会东县", "宁南县", "普格县", "布拖县", "金阳县", "昭觉县", "喜德县", "冕宁县", "越西县", "甘洛县", "美姑县", "雷波县")));
        regionInfo.add(new Province("四川省", sichuanCities));

// 贵州省
        List<City> guizhouCities = new ArrayList<>();
        guizhouCities.add(new City("贵阳市", List.of("南明区", "云岩区", "花溪区", "乌当区", "白云区", "观山湖区", "开阳县", "息烽县", "修文县", "清镇市")));
        guizhouCities.add(new City("六盘水市", List.of("钟山区", "水城区", "六枝特区", "盘州市")));
        guizhouCities.add(new City("遵义市", List.of("红花岗区", "汇川区", "播州区", "桐梓县", "绥阳县", "正安县", "道真仡佬族苗族自治县", "务川仡佬族苗族自治县", "凤冈县", "湄潭县", "余庆县", "习水县", "赤水市", "仁怀市")));
        guizhouCities.add(new City("安顺市", List.of("西秀区", "平坝区", "普定县", "镇宁布依族苗族自治县", "关岭布依族苗族自治县", "紫云苗族布依族自治县")));
        guizhouCities.add(new City("毕节市", List.of("七星关区", "大方县", "黔西县", "金沙县", "织金县", "纳雍县", "威宁彝族回族苗族自治县", "赫章县")));
        guizhouCities.add(new City("铜仁市", List.of("碧江区", "万山区", "江口县", "玉屏侗族自治县", "石阡县", "思南县", "印江土家族苗族自治县", "德江县", "沿河土家族自治县", "松桃苗族自治县")));
        guizhouCities.add(new City("黔西南布依族苗族自治州", List.of("兴义市", "兴仁市", "普安县", "晴隆县", "贞丰县", "望谟县", "册亨县", "安龙县")));
        guizhouCities.add(new City("黔东南苗族侗族自治州", List.of("凯里市", "黄平县", "施秉县", "三穗县", "镇远县", "岑巩县", "天柱县", "锦屏县", "剑河县", "台江县", "黎平县", "榕江县", "从江县", "雷山县", "麻江县", "丹寨县")));
        guizhouCities.add(new City("黔南布依族苗族自治州", List.of("都匀市", "福泉市", "荔波县", "贵定县", "瓮安县", "独山县", "平塘县", "罗甸县", "长顺县", "龙里县", "惠水县", "三都水族自治县")));
        regionInfo.add(new Province("贵州省", guizhouCities));

// 云南省
        List<City> yunnanCities = new ArrayList<>();
        yunnanCities.add(new City("昆明市", List.of("五华区", "盘龙区", "官渡区", "西山区", "东川区", "呈贡区", "晋宁区", "富民县", "宜良县", "石林彝族自治县", "嵩明县", "禄劝彝族苗族自治县", "寻甸回族彝族自治县", "安宁市")));
        yunnanCities.add(new City("曲靖市", List.of("麒麟区", "沾益区", "马龙区", "陆良县", "师宗县", "罗平县", "富源县", "会泽县", "宣威市")));
        yunnanCities.add(new City("玉溪市", List.of("红塔区", "江川区", "澄江市", "通海县", "华宁县", "易门县", "峨山彝族自治县", "新平彝族傣族自治县", "元江哈尼族彝族傣族自治县")));
        yunnanCities.add(new City("保山市", List.of("隆阳区", "施甸县", "龙陵县", "昌宁县", "腾冲市")));
        yunnanCities.add(new City("昭通市", List.of("昭阳区", "鲁甸县", "巧家县", "盐津县", "大关县", "永善县", "绥江县", "镇雄县", "彝良县", "威信县", "水富市")));
        yunnanCities.add(new City("丽江市", List.of("古城区", "玉龙纳西族自治县", "永胜县", "华坪县", "宁蒗彝族自治县")));
        yunnanCities.add(new City("普洱市", List.of("思茅区", "宁洱哈尼族彝族自治县", "墨江哈尼族自治县", "景东彝族自治县", "景谷傣族彝族自治县", "镇沅彝族哈尼族拉祜族自治县", "江城哈尼族彝族自治县", "孟连傣族拉祜族佤族自治县", "澜沧拉祜族自治县", "西盟佤族自治县")));
        yunnanCities.add(new City("临沧市", List.of("临翔区", "凤庆县", "云县", "永德县", "镇康县", "双江拉祜族佤族布朗族傣族自治县", "耿马傣族佤族自治县", "沧源佤族自治县")));
        yunnanCities.add(new City("楚雄彝族自治州", List.of("楚雄市", "双柏县", "牟定县", "南华县", "姚安县", "大姚县", "永仁县", "元谋县", "武定县", "禄丰县")));
        yunnanCities.add(new City("红河哈尼族彝族自治州", List.of("个旧市", "开远市", "蒙自市", "弥勒市", "屏边苗族自治县", "建水县", "石屏县", "泸西县", "元阳县", "红河县", "金平苗族瑶族傣族自治县", "绿春县", "河口瑶族自治县")));
        yunnanCities.add(new City("文山壮族苗族自治州", List.of("文山市", "砚山县", "西畴县", "麻栗坡县", "马关县", "丘北县", "广南县", "富宁县")));
        yunnanCities.add(new City("西双版纳傣族自治州", List.of("景洪市", "勐海县", "勐腊县")));
        yunnanCities.add(new City("大理白族自治州", List.of("大理市", "漾濞彝族自治县", "祥云县", "宾川县", "弥渡县", "南涧彝族自治县", "巍山彝族回族自治县", "永平县", "云龙县", "洱源县", "剑川县", "鹤庆县")));
        yunnanCities.add(new City("德宏傣族景颇族自治州", List.of("瑞丽市", "芒市", "梁河县", "盈江县", "陇川县")));
        yunnanCities.add(new City("怒江傈僳族自治州", List.of("泸水市", "福贡县", "贡山独龙族怒族自治县", "兰坪白族普米族自治县")));
        yunnanCities.add(new City("迪庆藏族自治州", List.of("香格里拉市", "德钦县", "维西傈僳族自治县")));
        regionInfo.add(new Province("云南省", yunnanCities));

// 陕西省
        List<City> shaanxiCities = new ArrayList<>();
        shaanxiCities.add(new City("西安市", List.of("新城区", "碑林区", "莲湖区", "灞桥区", "未央区", "雁塔区", "阎良区", "临潼区", "长安区", "高陵区", "鄠邑区", "蓝田县", "周至县")));
        shaanxiCities.add(new City("铜川市", List.of("王益区", "印台区", "耀州区", "宜君县")));
        shaanxiCities.add(new City("宝鸡市", List.of("渭滨区", "金台区", "陈仓区", "凤翔区", "岐山县", "扶风县", "眉县", "陇县", "千阳县", "麟游县", "凤县", "太白县")));
        shaanxiCities.add(new City("咸阳市", List.of("秦都区", "杨陵区", "渭城区", "三原县", "泾阳县", "乾县", "礼泉县", "永寿县", "彬州市", "长武县", "旬邑县", "淳化县", "武功县")));
        shaanxiCities.add(new City("渭南市", List.of("临渭区", "华州区", "潼关县", "大荔县", "合阳县", "澄城县", "蒲城县", "白水县", "富平县", "韩城市", "华阴市")));
        shaanxiCities.add(new City("延安市", List.of("宝塔区", "安塞区", "延长县", "延川县", "子长市", "志丹县", "吴起县", "甘泉县", "富县", "洛川县", "宜川县", "黄龙县", "黄陵县")));
        shaanxiCities.add(new City("汉中市", List.of("汉台区", "南郑区", "城固县", "洋县", "西乡县", "勉县", "宁强县", "略阳县", "镇巴县", "留坝县", "佛坪县")));
        shaanxiCities.add(new City("榆林市", List.of("榆阳区", "横山区", "府谷县", "靖边县", "定边县", "绥德县", "米脂县", "佳县", "吴堡县", "清涧县", "子洲县")));
        shaanxiCities.add(new City("安康市", List.of("汉滨区", "汉阴县", "石泉县", "宁陕县", "紫阳县", "岚皋县", "平利县", "镇坪县", "旬阳市", "白河县")));
        shaanxiCities.add(new City("商洛市", List.of("商州区", "洛南县", "丹凤县", "商南县", "山阳县", "镇安县", "柞水县")));
        regionInfo.add(new Province("陕西省", shaanxiCities));

// 甘肃省
        List<City> gansuCities = new ArrayList<>();
        gansuCities.add(new City("兰州市", List.of("城关区", "七里河区", "西固区", "安宁区", "红古区", "永登县", "皋兰县", "榆中县")));
        gansuCities.add(new City("嘉峪关市", List.of("嘉峪关市")));
        gansuCities.add(new City("金昌市", List.of("金川区", "永昌县")));
        gansuCities.add(new City("白银市", List.of("白银区", "平川区", "靖远县", "会宁县", "景泰县")));
        gansuCities.add(new City("天水市", List.of("秦州区", "麦积区", "清水县", "秦安县", "甘谷县", "武山县", "张家川回族自治县")));
        gansuCities.add(new City("武威市", List.of("凉州区", "民勤县", "古浪县", "天祝藏族自治县")));
        gansuCities.add(new City("张掖市", List.of("甘州区", "肃南裕固族自治县", "民乐县", "临泽县", "高台县", "山丹县")));
        gansuCities.add(new City("平凉市", List.of("崆峒区", "华亭市", "泾川县", "灵台县", "崇信县", "庄浪县", "静宁县")));
        gansuCities.add(new City("酒泉市", List.of("肃州区", "金塔县", "瓜州县", "肃北蒙古族自治县", "阿克塞哈萨克族自治县", "玉门市", "敦煌市")));
        gansuCities.add(new City("庆阳市", List.of("西峰区", "庆城县", "环县", "华池县", "合水县", "正宁县", "宁县", "镇原县")));
        gansuCities.add(new City("定西市", List.of("安定区", "通渭县", "陇西县", "渭源县", "临洮县", "漳县", "岷县")));
        gansuCities.add(new City("陇南市", List.of("武都区", "成县", "文县", "宕昌县", "康县", "西和县", "礼县", "徽县", "两当县")));
        gansuCities.add(new City("临夏回族自治州", List.of("临夏市", "临夏县", "康乐县", "永靖县", "广河县", "和政县", "东乡族自治县", "积石山保安族东乡族撒拉族自治县")));
        gansuCities.add(new City("甘南藏族自治州", List.of("合作市", "临潭县", "卓尼县", "舟曲县", "迭部县", "玛曲县", "碌曲县", "夏河县")));
        regionInfo.add(new Province("甘肃省", gansuCities));

        // 青海省
        List<City> qinghaiCities = new ArrayList<>();
        qinghaiCities.add(new City("西宁市", List.of("城东区", "城中区", "城西区", "城北区", "湟中区", "湟源县", "大通回族土族自治县")));
        qinghaiCities.add(new City("海东市", List.of("乐都区", "平安区", "民和回族土族自治县", "互助土族自治县", "化隆回族自治县", "循化撒拉族自治县")));
        qinghaiCities.add(new City("海北藏族自治州", List.of("海晏县", "祁连县", "刚察县", "门源回族自治县")));
        qinghaiCities.add(new City("黄南藏族自治州", List.of("同仁市", "尖扎县", "泽库县", "河南蒙古族自治县")));
        qinghaiCities.add(new City("海南藏族自治州", List.of("共和县", "同德县", "贵德县", "兴海县", "贵南县")));
        qinghaiCities.add(new City("果洛藏族自治州", List.of("玛沁县", "班玛县", "甘德县", "达日县", "久治县", "玛多县")));
        qinghaiCities.add(new City("玉树藏族自治州", List.of("玉树市", "杂多县", "称多县", "治多县", "囊谦县", "曲麻莱县")));
        qinghaiCities.add(new City("海西蒙古族藏族自治州", List.of("格尔木市", "德令哈市", "茫崖市", "乌兰县", "都兰县", "天峻县")));
        regionInfo.add(new Province("青海省", qinghaiCities));


// 内蒙古自治区
        List<City> neimengguCities = new ArrayList<>();
        neimengguCities.add(new City("呼和浩特市", List.of("新城区", "回民区", "玉泉区", "赛罕区", "土默特左旗", "托克托县", "和林格尔县", "清水河县", "武川县")));
        neimengguCities.add(new City("包头市", List.of("东河区", "昆都仑区", "青山区", "石拐区", "白云鄂博矿区", "九原区", "土默特右旗", "固阳县", "达尔罕茂明安联合旗")));
        neimengguCities.add(new City("乌海市", List.of("海勃湾区", "海南区", "乌达区")));
        neimengguCities.add(new City("赤峰市", List.of("红山区", "元宝山区", "松山区", "阿鲁科尔沁旗", "巴林左旗", "巴林右旗", "林西县", "克什克腾旗", "翁牛特旗", "喀喇沁旗", "宁城县", "敖汉旗")));
        neimengguCities.add(new City("通辽市", List.of("科尔沁区", "科尔沁左翼中旗", "科尔沁左翼后旗", "开鲁县", "库伦旗", "奈曼旗", "扎鲁特旗", "霍林郭勒市")));
        neimengguCities.add(new City("鄂尔多斯市", List.of("东胜区", "康巴什区", "达拉特旗", "准格尔旗", "鄂托克前旗", "鄂托克旗", "杭锦旗", "乌审旗", "伊金霍洛旗")));
        neimengguCities.add(new City("呼伦贝尔市", List.of("海拉尔区", "扎赉诺尔区", "阿荣旗", "莫力达瓦达斡尔族自治旗", "鄂伦春自治旗", "鄂温克族自治旗", "陈巴尔虎旗", "新巴尔虎左旗", "新巴尔虎右旗", "满洲里市", "牙克石市", "扎兰屯市", "额尔古纳市", "根河市")));
        neimengguCities.add(new City("巴彦淖尔市", List.of("临河区", "五原县", "磴口县", "乌拉特前旗", "乌拉特中旗", "乌拉特后旗", "杭锦后旗")));
        neimengguCities.add(new City("乌兰察布市", List.of("集宁区", "卓资县", "化德县", "商都县", "兴和县", "凉城县", "察哈尔右翼前旗", "察哈尔右翼中旗", "察哈尔右翼后旗", "四子王旗", "丰镇市")));
        neimengguCities.add(new City("兴安盟", List.of("乌兰浩特市", "阿尔山市", "科尔沁右翼前旗", "科尔沁右翼中旗", "扎赉特旗", "突泉县")));
        neimengguCities.add(new City("锡林郭勒盟", List.of("二连浩特市", "锡林浩特市", "阿巴嘎旗", "苏尼特左旗", "苏尼特右旗", "东乌珠穆沁旗", "西乌珠穆沁旗", "太仆寺旗", "镶黄旗", "正镶白旗", "正蓝旗", "多伦县")));
        neimengguCities.add(new City("阿拉善盟", List.of("阿拉善左旗", "阿拉善右旗", "额济纳旗")));
        regionInfo.add(new Province("内蒙古自治区", neimengguCities));

// 广西壮族自治区
        List<City> guangxiCities = new ArrayList<>();
        guangxiCities.add(new City("南宁市", List.of("兴宁区", "青秀区", "江南区", "西乡塘区", "良庆区", "邕宁区", "武鸣区", "隆安县", "马山县", "上林县", "宾阳县", "横州市")));
        guangxiCities.add(new City("柳州市", List.of("城中区", "鱼峰区", "柳南区", "柳北区", "柳江区", "柳城县", "鹿寨县", "融安县", "融水苗族自治县", "三江侗族自治县")));
        guangxiCities.add(new City("桂林市", List.of("秀峰区", "叠彩区", "象山区", "七星区", "雁山区", "临桂区", "阳朔县", "灵川县", "全州县", "兴安县", "永福县", "灌阳县", "龙胜各族自治县", "资源县", "平乐县", "荔浦市", "恭城瑶族自治县")));
        guangxiCities.add(new City("梧州市", List.of("万秀区", "长洲区", "龙圩区", "苍梧县", "藤县", "蒙山县", "岑溪市")));
        guangxiCities.add(new City("北海市", List.of("海城区", "银海区", "铁山港区", "合浦县")));
        guangxiCities.add(new City("防城港市", List.of("港口区", "防城区", "上思县", "东兴市")));
        guangxiCities.add(new City("钦州市", List.of("钦南区", "钦北区", "灵山县", "浦北县")));
        guangxiCities.add(new City("贵港市", List.of("港北区", "港南区", "覃塘区", "平南县", "桂平市")));
        guangxiCities.add(new City("玉林市", List.of("玉州区", "福绵区", "容县", "陆川县", "博白县", "兴业县", "北流市")));
        guangxiCities.add(new City("百色市", List.of("右江区", "田阳区", "田东县", "平果市", "德保县", "那坡县", "凌云县", "乐业县", "田林县", "西林县", "隆林各族自治县")));
        guangxiCities.add(new City("贺州市", List.of("八步区", "平桂区", "昭平县", "钟山县", "富川瑶族自治县")));
        guangxiCities.add(new City("河池市", List.of("金城江区", "宜州区", "南丹县", "天峨县", "凤山县", "东兰县", "罗城仫佬族自治县", "环江毛南族自治县", "巴马瑶族自治县", "都安瑶族自治县", "大化瑶族自治县")));
        guangxiCities.add(new City("来宾市", List.of("兴宾区", "象州县", "武宣县", "金秀瑶族自治县", "忻城县", "合山市")));
        guangxiCities.add(new City("崇左市", List.of("江州区", "扶绥县", "宁明县", "龙州县", "大新县", "天等县", "凭祥市")));
        regionInfo.add(new Province("广西壮族自治区", guangxiCities));

// 西藏自治区
        List<City> xizangCities = new ArrayList<>();
        xizangCities.add(new City("拉萨市", List.of("城关区", "堆龙德庆区", "达孜区", "林周县", "当雄县", "尼木县", "曲水县", "墨竹工卡县")));
        xizangCities.add(new City("日喀则市", List.of("桑珠孜区", "南木林县", "江孜县", "定日县", "萨迦县", "拉孜县", "昂仁县", "谢通门县", "白朗县", "仁布县", "康马县", "定结县", "仲巴县", "亚东县", "吉隆县", "聂拉木县", "萨嘎县", "岗巴县")));
        xizangCities.add(new City("昌都市", List.of("卡若区", "江达县", "贡觉县", "类乌齐县", "丁青县", "察雅县", "八宿县", "左贡县", "芒康县", "洛隆县", "边坝县")));
        xizangCities.add(new City("林芝市", List.of("巴宜区", "工布江达县", "米林县", "墨脱县", "波密县", "察隅县", "朗县")));
        xizangCities.add(new City("山南市", List.of("乃东区", "扎囊县", "贡嘎县", "桑日县", "琼结县", "曲松县", "措美县", "洛扎县", "加查县", "隆子县", "错那县", "浪卡子县")));
        xizangCities.add(new City("那曲市", List.of("色尼区", "嘉黎县", "比如县", "聂荣县", "安多县", "申扎县", "索县", "班戈县", "巴青县", "尼玛县", "双湖县")));
        xizangCities.add(new City("阿里地区", List.of("噶尔县", "普兰县", "札达县", "日土县", "革吉县", "改则县", "措勤县")));
        regionInfo.add(new Province("西藏自治区", xizangCities));
// 宁夏回族自治区
        List<City> ningxiaCities = new ArrayList<>();
        ningxiaCities.add(new City("银川市", List.of("兴庆区", "西夏区", "金凤区", "永宁县", "贺兰县", "灵武市")));
        ningxiaCities.add(new City("石嘴山市", List.of("大武口区", "惠农区", "平罗县")));
        ningxiaCities.add(new City("吴忠市", List.of("利通区", "红寺堡区", "盐池县", "同心县", "青铜峡市")));
        ningxiaCities.add(new City("固原市", List.of("原州区", "西吉县", "隆德县", "泾源县", "彭阳县")));
        ningxiaCities.add(new City("中卫市", List.of("沙坡头区", "中宁县", "海原县")));
        regionInfo.add(new Province("宁夏回族自治区", ningxiaCities));

// 新疆维吾尔自治区
        List<City> xinjiangCities = new ArrayList<>();
        xinjiangCities.add(new City("乌鲁木齐市", List.of("天山区", "沙依巴克区", "新市区", "水磨沟区", "头屯河区", "达坂城区", "米东区", "乌鲁木齐县")));
        xinjiangCities.add(new City("克拉玛依市", List.of("独山子区", "克拉玛依区", "白碱滩区", "乌尔禾区")));
        xinjiangCities.add(new City("吐鲁番市", List.of("高昌区", "鄯善县", "托克逊县")));
        xinjiangCities.add(new City("哈密市", List.of("伊州区", "巴里坤哈萨克自治县", "伊吾县")));
        xinjiangCities.add(new City("昌吉回族自治州", List.of("昌吉市", "阜康市", "呼图壁县", "玛纳斯县", "奇台县", "吉木萨尔县", "木垒哈萨克自治县")));
        xinjiangCities.add(new City("博尔塔拉蒙古自治州", List.of("博乐市", "阿拉山口市", "精河县", "温泉县")));
        xinjiangCities.add(new City("巴音郭楞蒙古自治州", List.of("库尔勒市", "轮台县", "尉犁县", "若羌县", "且末县", "焉耆回族自治县", "和静县", "和硕县", "博湖县")));
        xinjiangCities.add(new City("阿克苏地区", List.of("阿克苏市", "温宿县", "库车市", "沙雅县", "新和县", "拜城县", "乌什县", "阿瓦提县", "柯坪县")));
        xinjiangCities.add(new City("克孜勒苏柯尔克孜自治州", List.of("阿图什市", "阿克陶县", "阿合奇县", "乌恰县")));
        xinjiangCities.add(new City("喀什地区", List.of("喀什市", "疏附县", "疏勒县", "英吉沙县", "泽普县", "莎车县", "叶城县", "麦盖提县", "岳普湖县", "伽师县", "巴楚县", "塔什库尔干塔吉克自治县")));
        xinjiangCities.add(new City("和田地区", List.of("和田市", "和田县", "墨玉县", "皮山县", "洛浦县", "策勒县", "于田县", "民丰县")));
        xinjiangCities.add(new City("伊犁哈萨克自治州", List.of("伊宁市", "奎屯市", "霍尔果斯市", "伊宁县", "察布查尔锡伯自治县", "霍城县", "巩留县", "新源县", "昭苏县", "特克斯县", "尼勒克县")));
        xinjiangCities.add(new City("塔城地区", List.of("塔城市", "乌苏市", "沙湾市", "额敏县", "托里县", "裕民县", "和布克赛尔蒙古自治县")));
        xinjiangCities.add(new City("阿勒泰地区", List.of("阿勒泰市", "布尔津县", "富蕴县", "福海县", "哈巴河县", "青河县", "吉木乃县")));
        regionInfo.add(new Province("新疆维吾尔自治区", xinjiangCities));

    }
}