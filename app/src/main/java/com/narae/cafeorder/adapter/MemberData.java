package com.narae.cafeorder.adapter;

//Member의 정보를 저장하기 위한 클래스.....
public class MemberData {

    String name;    //이름 저장
    String nation;   //국적 저장
    int imgId;      //국기 이미지의 리소스 아이디

    public MemberData(String name, String nation, int imgId) {
        // TODO Auto-generated constructor stub
        //생성자함수로 전달받은 Member의 정보를 멤버변수에 저장..
        this.name= name;
        this.nation=nation;
        this.imgId=imgId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getName() {
        return name;
    }

    public String getNation() {
        return nation;
    }

    public int getImgId() {
        return imgId;
    }


}
