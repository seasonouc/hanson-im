package com.hanson.im.common.utils;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * @author hanson
 * @Date 2019/1/18
 * @Description:
 */
public class VailidatorTest {

    @Test
    public void testVailidateUserId() throws Exception {
        Assert.assertFalse(Vailidator.vailidateUserId("1234"));
        Assert.assertFalse(Vailidator.vailidateUserId(null));
        Assert.assertFalse(Vailidator.vailidateUserId(""));
        Assert.assertTrue(Vailidator.vailidateUserId("12345fdG"));
    }
}