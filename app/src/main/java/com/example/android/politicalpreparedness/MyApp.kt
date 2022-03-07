package com.example.android.politicalpreparedness

import android.app.Application
import com.example.android.politicalpreparedness.data.ElectionRepository
import com.example.android.politicalpreparedness.data.PoliticalPreparednessProvider
import com.example.android.politicalpreparedness.data.database.ElectionDatabase
import com.example.android.politicalpreparedness.data.network.CivicsApiService
import com.example.android.politicalpreparedness.data.network.RetrofitProvider
import com.example.android.politicalpreparedness.data.network.jsonadapter.ElectionAdapter
import com.example.android.politicalpreparedness.election.ElectionsViewModel
import com.example.android.politicalpreparedness.election.VoterInfoViewModel
import com.example.android.politicalpreparedness.representative.RepresentativeViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import java.util.*

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        val coroutineContextProviderModule = module {
            single<CoroutineContextProvider> {
                CoroutineContextProvider.Default()
            }
        }

        val viewModelModule = module {
            viewModel { ElectionsViewModel(get()) }
            viewModel { RepresentativeViewModel(get()) }
            viewModel { VoterInfoViewModel(get()) }
        }

        val dataModules = module {
            factory { PoliticalPreparednessProvider(get(), get()) }
            single { ElectionRepository(get()) }
            single { ElectionDatabase.getInstance(this@MyApp).electionDao }
        }

        val networkModule = module {
            single {
                Moshi.Builder()
                    .add(ElectionAdapter())
                    .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
                    .add(KotlinJsonAdapterFactory())
                    .build()
            }
            single {
                RetrofitProvider(get(), API_BASE_URL).provide()
            }
            single {
                get<Retrofit>().create(CivicsApiService::class.java)
            }
        }

        startKoin {
            androidLogger()
            androidContext(this@MyApp)
            modules(
                listOf(
                    viewModelModule,
                    dataModules,
                    networkModule
                )
            )
        }
    }

    companion object {
        const val API_BASE_URL = "https://www.googleapis.com/civicinfo/v2/"
    }
}
