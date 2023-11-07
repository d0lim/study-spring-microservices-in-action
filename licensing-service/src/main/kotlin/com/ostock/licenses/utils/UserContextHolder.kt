package com.ostock.licenses.utils

import org.springframework.util.Assert

class UserContextHolder {
    companion object {
        private val userContext = ThreadLocal<UserContext>()

        fun getContext(): UserContext {
            var context = userContext.get()
            if (context == null) {
                context = createEmptyContext()
                userContext.set(context)
            }
            return userContext.get()
        }

        fun setContext(context: UserContext) {
            Assert.notNull(context, "Only non-null UserContext instances are permitted")
            userContext.set(context)
        }

        fun createEmptyContext(): UserContext {
            return UserContext()
        }
    }
}
