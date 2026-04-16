package com.jvckenwood.applauncher.applist;

import com.jvckenwood.applauncher.tools.AppInfo;
import com.jvckenwood.tools.AppLog;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/* JADX INFO: loaded from: classes.dex */
public class AppListParser {
    private Document mDocument;
    private XPath mXPath;
    private Properties _whiteList = new Properties();
    String _dPName = "";
    int _dRestrictLv = 6;
    String _dCertHash = "";

    public AppListParser(InputStream inputStream) throws Exception {
        initWhiteListDocument(inputStream);
    }

    private void initWhiteListDocument(InputStream inputStream) throws Exception {
        this._whiteList.load(inputStream);
    }

    public int getNumberOfIndex() {
        try {
            NodeList textList = (NodeList) this.mXPath.evaluate("/white-list/app", this.mDocument, XPathConstants.NODESET);
            int count = textList.getLength();
            return count;
        } catch (XPathExpressionException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private String getPropertyString(String keyStr, String defaultStr) {
        try {
            String result = this._whiteList.getProperty(keyStr).toString();
            return result;
        } catch (Exception e) {
            return defaultStr;
        }
    }

    private int getPropertyInteger(String keyStr, int defaultValue) {
        try {
            String valStr = this._whiteList.getProperty(keyStr).toString();
            int result = Integer.parseInt(valStr);
            return result;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public AppInfo getListContents(int position) throws Exception {
        try {
            String indexStr = String.valueOf(position);
            String pName = getPropertyString(indexStr + ".pname", this._dPName);
            if (pName.isEmpty()) {
                return null;
            }
            String appName = getPropertyString(indexStr + ".appName", pName);
            int restrictLv = getPropertyInteger(indexStr + ".restrictLv", this._dRestrictLv);
            List<String> hashList = new ArrayList<>();
            int i = 0;
            while (true) {
                int num = i + 1;
                String hash = getPropertyString(indexStr + ".certHash" + num, this._dCertHash);
                if (!hash.isEmpty()) {
                    hashList.add(hash);
                    i++;
                } else {
                    return new AppInfo(appName, pName, restrictLv, hashList);
                }
            }
        } catch (Exception e) {
            AppLog.d("AppListParser", e.getMessage(), e);
            return null;
        }
    }
}
