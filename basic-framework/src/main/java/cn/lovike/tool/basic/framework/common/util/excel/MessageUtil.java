package cn.lovike.tool.basic.framework.common.util.excel;

import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
 * @author lovike
 * 一些比较恶心比如的excel表头直接在这里翻译
 * @since 2020/10/29
 */
public class MessageUtil {
    private MessageUtil() {
    }

    private static Locale getLocale() {
        return LocaleContextHolder.getLocale();
    }

    public static String[] getExportByPolicyTitles() {
        Locale   locale = getLocale();
        String[] titles;
//        if ("en".equals(locale.getLanguage())) {
//            titles = new String[]{"Name of Policy Set", "Total Input", "Pass/Pass Rate", "Reject Rate/Reject Rate", "Manual Audit Rate/Manual Audit Rate"};
//        } else {
//            titles = new String[]{"策略集名称", "进件总量", "通过量/通过率", "拒绝量/拒绝率", "人工审核量/人工审核率"};
//        }
        titles = new String[]{"策略集名称", "进件总量", "通过量/通过率", "拒绝量/拒绝率", "人工审核量/人工审核率"};
        return titles;
    }

    public static String[] getPermListTitles() {
        Locale   locale = getLocale();
        String[] titles;
//        if ("en".equals(locale.getLanguage())) {
//            titles = new String[]{"Name of Policy Set", "Total Input", "Pass/Pass Rate", "Reject Rate/Reject Rate", "Manual Audit Rate/Manual Audit Rate"};
//        } else {
//            titles = new String[]{"策略集名称", "进件总量", "通过量/通过率", "拒绝量/拒绝率", "人工审核量/人工审核率"};
//        }
        titles = new String[]{"id", "权限名称", "url"};
        return titles;
    }
}
