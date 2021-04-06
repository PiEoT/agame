// Decompiled by JEB v1.5.201408040 - http://www.android-decompiler.com

package com.agame.http.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Pair;

public class IniReader {
        public class Section {
                public String name;
                public List<Pair<String, String>> datas;
                public IniReader iniReader;

                public Section(IniReader arg2, String n) {
                        this.iniReader = arg2;
                        this.datas = new ArrayList<Pair<String, String>>();
                        this.name = n;
                }

                public String find(String n) {
                        for (Pair<String, String> pair : datas) {
                                if (n.equalsIgnoreCase(pair.first)) {
                                        return pair.second;
                                }
                        }
                        return "";
                        // Object v0;
                        // Iterator v2 = this.datas.iterator();
                        // do {
                        // if(v2.hasNext()) {
                        // v0 = v2.next();
                        // if(!((Pair)v0).first.equalsIgnoreCase(n)) {
                        // continue;
                        // }
                        //
                        // break;
                        // }
                        // else {
                        // goto label_4;
                        // }
                        // }
                        // while(true);
                        //
                        // Object v1 = ((Pair)v0).second;
                        // goto label_5;
                        // label_4:
                        // String v1_1 = "";
                        // label_5:
                        // return ((String)v1);
                }
        }

        public Map<String, Section> sections;

        public IniReader(Reader fs) throws IOException {
                this.sections = new HashMap<String, IniReader.Section>();
                BufferedReader v0 = new BufferedReader(fs);
                this.readIni(v0, this.createSection(""));
                v0.close();
        }

        public void Clear() {
                this.sections.clear();
        }

        public Section createSection(String name) {
                Object v0 = this.sections.get(name);
                if (v0 == null) {
                        Section v0_1 = new Section(this, name);
                        this.sections.put(name, v0_1);
                }

                return ((Section) v0);
        }

        public Section getSection(String name) {
                return this.sections.get(name);
        }

        public String getValue(String section, String name) {
                Section v0 = this.getSection(section);
                String v1 = v0 == null ? "" : v0.find(name);
                return v1;
        }

        public Section parseLine(Section sec, String line) {
                Section v3;
                if (line == null) {
                        v3 = null;
                } else {
                        line = line.trim();
                        if (line.length() > 0 && line.charAt(0) != 39) {
                                if (line.matches("\\[.*\\]")) {
                                        sec = this.createSection(line.replaceFirst("\\[(.*)\\]", "$1"));
                                } else if (line.matches(".*=.*")) {
                                        int v0 = line.indexOf(61);
                                        sec.datas.add(new Pair(line.substring(0, v0).trim(), line.substring(v0 + 1).trim()));
                                }

                                return sec;
                        }

                        v3 = sec;
                }

                return v3;
        }

        public void readIni(BufferedReader reader, Section sec) throws IOException {
                while (sec != null) {
                        sec = this.parseLine(sec, reader.readLine());
                }
        }
}
