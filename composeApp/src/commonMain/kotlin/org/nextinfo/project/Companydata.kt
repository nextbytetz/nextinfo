package org.nextinfo.project


/**
 * @param logoUrl  Remote URL or null → fall back to [initials].
 * @param logoRes  Local drawable resource name or null.
 */
data class Company(
    val name: String,
    val logoUrl: String? = null,
    val logoRes: String? = null,
    val tags: List<String>,
    val subscribers: String,
    val category: String
)


/** Returns up-to-2-letter initials from a company name. */
fun initials(name: String): String =
    name.trim()
        .split("\\s+".toRegex())
        .filter { it.isNotEmpty() }
        .take(2)
        .joinToString("") { it.first().uppercaseChar().toString() }

/** First [FEATURED_COUNT] companies are treated as featured (highest backend rating). */
const val FEATURED_COUNT = 5


val tanzaniaCompanies: List<Company> = listOf(
    // Top-5 Featured (highest backend rating)
    Company(
        name = "Vodacom Tanzania",
        tags = listOf("UPDATES", "BILLING", "PROMOTIONS"),
        subscribers = "1.5M",
        category = "TELECOM"
    ),
    Company(
        name = "Nextbyte ICT Solutions",
        tags = listOf("UPDATES", "BILLING", "OFFERS", "REMINDERS", "SUPPORT"),
        subscribers = "1.1M",
        category = "TECH"
    ),
    Company(
        name = "NMB Bank",
        tags = listOf("FINANCE", "PAYMENTS", "LOANS"),
        subscribers = "950K",
        category = "FINANCE"
    ),
    Company(
        name = "CRDB Bank",
        tags = listOf("BANKING", "ALERTS", "INVESTMENTS"),
        subscribers = "1.2M",
        category = "BANKING"
    ),
    Company(
        name = "Azam TV",
        tags = listOf("SCHEDULES", "OFFERS", "LIVE"),
        subscribers = "720K",
        category = "MEDIA"
    ),

    Company(
        name = "Airtel Tanzania",
        tags = listOf("UPDATES", "DATA BUNDLES"),
        subscribers = "980K",
        category    = "TELECOM"
    ),
    Company(
        name = "Tanzania Revenue Authority",
        tags = listOf("TAX", "COMPLIANCE", "E-FILING"),
        subscribers = "620K",
        category = "GOVERNMENT"
    ),
    Company(
        name = "Equity Bank Tanzania",
        tags = listOf("BANKING", "SAVINGS", "LOANS"),
        subscribers = "410K",
        category = "BANKING"
    ),
    Company(
        name = "Tanzanian Breweries Limited",
        tags = listOf("OFFERS", "EVENTS"),
        subscribers = "530K",
        category = "FMCG"
    ),
    Company(
        name = "Dar es Salaam Stock Exchange",
        tags = listOf("MARKETS", "TRADING", "IPO ALERTS"),
        subscribers = "390K",
        category = "FINANCE"
    ),
    Company(
        name = "ITV Tanzania",
        tags = listOf("NEWS", "LIVE TV", "SCHEDULES"),
        subscribers = "680K",
        category = "MEDIA"
    ),
    Company(
        name = "Standard Chartered Tanzania",
        tags = listOf("BANKING", "FOREX", "WEALTH MANAGEMENT"),
        subscribers = "310K",
        category = "BANKING"
    ),
    Company(
        name = "Tanzania Electric Supply Company",
        tags = listOf("OUTAGES", "BILLING", "MAINTENANCE ALERTS"),
        subscribers = "1.3M",
        category = "UTILITIES"
    ),
    Company(
        name = "Zan Telecom",
        tags = listOf("CALLS", "DATA"),
        subscribers = "270K",
        category = "TELECOM"
    ),
    Company(
        name = "National Microfinance Bank",
        tags = listOf("FINANCE", "MICROLOANS", "SAVINGS"),
        subscribers = "490K",
        category = "FINANCE"
    ),
    Company(
        name = "Precision Air Services",
        tags = listOf("FLIGHTS", "DEALS", "SCHEDULES"),
        subscribers = "360K",
        category = "TRAVEL"
    ),
    Company(
        name = "Multichoice Tanzania",
        tags = listOf("DSTV", "GOTV", "SCHEDULES", "OFFERS"),
        subscribers = "540K",
        category = "MEDIA"
    ),
    Company(
        name = "Tanzania Posts Corporation",
        tags = listOf("PARCELS", "TRACKING", "EMS"),
        subscribers = "210K",
        category = "LOGISTICS"
    ),
    Company(
        name = "KCB Bank Tanzania",
        tags = listOf("BANKING", "M-PESA LINK", "LOANS"),
        subscribers = "450K",
        category = "BANKING"
    ),
    Company(
        name = "Simba Sports Club",
        tags = listOf("MATCHES", "TICKETS", "TRANSFERS"),
        subscribers = "820K",
        category = "SPORTS"
    ),
    Company(
        name = "Yanga Sports Club",
        tags = listOf("MATCHES", "TICKETS", "NEWS"),
        subscribers = "800K",
        category = "SPORTS"
    ),
    Company(
        name = "National Health Insurance Fund",
        tags = listOf("NHIF", "CLAIMS", "MEMBERSHIP"),
        subscribers = "1.0M",
        category = "HEALTH"
    ),
    Company(
        name = "Tanzania Broadcasting Corporation",
        tags = listOf("NEWS", "RADIO", "TV"),
        subscribers = "590K",
        category = "MEDIA"
    ),
    Company(
        name = "Halotel Tanzania",
        tags = listOf("DATA", "CALLS", "PROMOTIONS"),
        subscribers = "430K",
        category = "TELECOM"
    ),
    Company(
        name = "Jubilee Insurance Tanzania",
        tags = listOf("INSURANCE", "CLAIMS", "RENEWALS"),
        subscribers = "290K",
        category = "INSURANCE"
    )
)