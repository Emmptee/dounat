package cn.com.bis.addresslibrary.service;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

import cn.com.bis.addresslibrary.model.CityModel;
import cn.com.bis.addresslibrary.model.DistrictModel;
import cn.com.bis.addresslibrary.model.ProvinceModel;


public class XmlParserHandler extends DefaultHandler {

	/**
	 * �洢���еĽ�������
	 */
	private List<ProvinceModel> provinceList = new ArrayList<ProvinceModel>();
	 	  
	public XmlParserHandler() {
		
	}

	public List<ProvinceModel> getDataList() {
		return provinceList;
	}

	@Override
	public void startDocument() throws SAXException {
		// ��������һ����ʼ��ǩ��ʱ�򣬻ᴥ���������
	}

	ProvinceModel provinceModel = new ProvinceModel();
	CityModel cityModel = new CityModel();
	DistrictModel districtModel = new DistrictModel();
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// ��������ʼ��ǵ�ʱ�򣬵����������
		if ("province".equals(qName)) {
			provinceModel = new ProvinceModel();
			provinceModel.setName(attributes.getValue(0));
			provinceModel.setCityList(new ArrayList<CityModel>());
		} else if ("city".equals(qName)) {
			cityModel = new CityModel();
			cityModel.setName(attributes.getValue(0));
			cityModel.setDistrictList(new ArrayList<DistrictModel>());
		} else if ("district".equals(qName)) {
			districtModel = new DistrictModel();
			districtModel.setName(attributes.getValue(0));
			districtModel.setZipcode(attributes.getValue(1));
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// ���������ǵ�ʱ�򣬻�����������
		if ("district".equals(qName)) {
			cityModel.getDistrictList().add(districtModel);
        } else if ("city".equals(qName)) {
        	provinceModel.getCityList().add(cityModel);
        } else if ("province".equals(qName)) {
        	provinceList.add(provinceModel);
        }
	}
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
	}

}
