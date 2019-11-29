package com.example.tagihin.di

import com.example.tagihin.utils.rule.NotEmptyRule
import org.koin.dsl.module

val ruleSource = module {
    single { NotEmptyRule() }
}