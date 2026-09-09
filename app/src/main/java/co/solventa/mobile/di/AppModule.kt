package co.solventa.mobile.di

import co.solventa.mobile.data.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds abstract fun bindAuthRepository(value: FakeAuthRepository): AuthRepository
    @Binds abstract fun bindInsuranceRepository(value: FakeInsuranceRepository): InsuranceRepository
}
