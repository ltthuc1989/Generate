package com.ltthuc.feature.di.imp

import com.ltthuc.ads.AdsSettings
import com.ltthuc.feature.R
import com.ltthuc.ui.adapter.MoreAppRowData
import com.ltthuc.ui.base.settings.IFeatureSetting


class FeatureSettingImp: IFeatureSetting {
    override fun updateAdsConfig() {
        AdsSettings.isOtherAppShowing = true
    }

    override fun getAppList(): List<MoreAppRowData> {
        return arrayListOf(
            MoreAppRowData(
                R.drawable.icon_app_heatindex,
                "Heat Index Calculator - How to",
                "trithuc.heatindex.calc"
            ),
            MoreAppRowData(
                R.drawable.icon_app_airquality,
                "Air quality Index - PM2.5",
                "com.trithuc.airquality"
            ),
            MoreAppRowData(
                R.drawable.icon_app_fluent_calc,
                "Fluent Stuttering Calculator",
                "com.trithuc.fluencycalculator"
            ),
            MoreAppRowData(
                R.drawable.icon_app_mortgage_calc,
                "Mortgage Payoff Calculator",
                "com.trithuc.mortgagecalc"
            ),
            MoreAppRowData(
                R.drawable.icon_app_investment_calc,
                "Investment Calculator - Calc",
                "trithuc.investment.calc"
            ),
            MoreAppRowData(
                R.drawable.icon_app_atmortizaiton_calc,
                "Amortizing Loan Calculator",
                "trithuc.amortization.calc"
            ),
            MoreAppRowData(
                R.drawable.icon_app_cd_calculator,
                "Cd Calculator - Calc",
                "com.trithuc.cdcalculator"
            ),
            MoreAppRowData(
                R.drawable.icon_app_dewpoint,
                "Dew Point Humidity Calculator",
                "com.trithuc.dewpoint"
            ),
            MoreAppRowData(
                R.drawable.icon_app_windchill,
                "Wind chill & Humid Calculators",
                "trithuc.windchill.calc"
            ),
            MoreAppRowData(
                R.drawable.icon_app_positionsize,
                "Position Size Lots Pip Calc Fx",
                "com.trithuc.positionsize.app"
            )
        )
    }
}