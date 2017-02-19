/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boomerang.rnn.util;

import java.util.Comparator;
import java.util.List;

/**
 *
 * @author kcarava
 */
public class RowComparator implements Comparator<List<String>> {
    @Override
    public int compare(List<String> a, List<String> b) {
        int days0 = Integer.valueOf(a.get(0));
        int days1 = Integer.valueOf(b.get(0));
        return Integer.compare(days1, days0);
    }
}
