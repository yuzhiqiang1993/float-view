package com.yzq.floating_view

/**
 * Activity 过滤规则
 * @author yuzhiqiang
 */
sealed class ActivityFilterRule {

    /**
     * 判断给定的 Activity 类是否匹配当前规则
     *
     * @param activityClass Activity 类
     * @return 是否匹配
     */
    abstract fun matches(activityClass: Class<*>): Boolean

    /**
     * 类型匹配规则
     *
     * 精确匹配指定的 Activity 类型
     *
     * @param clazz Activity 类
     */
    data class ClassRule(val clazz: Class<*>) : ActivityFilterRule() {
        override fun matches(activityClass: Class<*>): Boolean {
            return activityClass == clazz
        }
    }

    /**
     * 包名匹配规则
     *
     * 匹配指定包名下的所有 Activity（支持前缀匹配）
     *
     * 示例：
     * - "com.example.feature" 会匹配 "com.example.feature.MainActivity"
     * - "com.example.feature" 会匹配 "com.example.feature.screen.DetailActivity"
     *
     * @param packageName 包名
     */
    data class PackageRule(val packageName: String) : ActivityFilterRule() {
        override fun matches(activityClass: Class<*>): Boolean {
            return activityClass.name.startsWith("$packageName.")
        }
    }

    /**
     * 类名匹配规则
     *
     * 支持简单类名或全限定类名匹配
     *
     * 示例：
     * - "MainActivity" 会匹配任意包下的 MainActivity
     * - "com.example.MainActivity" 会精确匹配该类
     *
     * @param className 类名（简单名或全限定名）
     */
    data class ClassNameRule(val className: String) : ActivityFilterRule() {
        override fun matches(activityClass: Class<*>): Boolean {
            return activityClass.simpleName == className || activityClass.name == className
        }
    }

    companion object {
        /**
         * 创建类型匹配规则
         *
         * @param clazz Activity 类
         * @return ClassRule
         */
        @JvmStatic
        fun ofClass(clazz: Class<*>): ActivityFilterRule = ClassRule(clazz)

        /**
         * 创建包名匹配规则
         *
         * @param packageName 包名
         * @return PackageRule
         */
        @JvmStatic
        fun ofPackage(packageName: String): ActivityFilterRule = PackageRule(packageName)

        /**
         * 创建类名匹配规则
         *
         * @param className 类名（简单名或全限定名）
         * @return ClassNameRule
         */
        @JvmStatic
        fun ofClassName(className: String): ActivityFilterRule = ClassNameRule(className)
    }
}
