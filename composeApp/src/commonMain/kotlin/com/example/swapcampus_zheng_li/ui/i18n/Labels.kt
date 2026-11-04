package com.example.swapcampus_zheng_li.ui.i18n
import androidx.compose.runtime.Composable
import com.example.swapcampus_zheng_li.*
import com.example.swapcampus_zheng_li.composeapp.resources.*
import org.jetbrains.compose.resources.*
@Composable
fun Categorie.label(): String = stringResource(when(this) {
    Categorie.LIVRES -> Res.string.category_books
    Categorie.COURS -> Res.string.category_courses
    Categorie.ELECTRONIQUE -> Res.string.category_electronics
    Categorie.SPORT_LOISIRS -> Res.string.category_sport
    Categorie.MOBILIER -> Res.string.category_furniture
    Categorie.VETEMENTS -> Res.string.category_clothes
    Categorie.AUTRE -> Res.string.category_other
})
@Composable
fun EtatAnnonce.label(): String = when(this) {
 Disponible -> stringResource(Res.string.available)
 is Reservee -> stringResource(Res.string.reserved_by, acheteur)
 is Vendue -> stringResource(Res.string.sold_on, formatDate(dateVente, currentLanguageTag()))
}
fun listingErrorResource(message: String?): StringResource = when(message) {
 "Please log in first." -> Res.string.login_required
 "Listing not found." -> Res.string.not_found
 "Annonce introuvable." -> Res.string.not_found
 "Le titre est obligatoire." -> Res.string.error_title
 "Le prix doit être supérieur à zéro." -> Res.string.error_positive_price
 "Only the owner can edit this listing." -> Res.string.error_edit_owner
 "This listing cannot be reserved." -> Res.string.error_reserve
 "Invalid buyer." -> Res.string.error_buyer
 "Only the owner can complete a reservation." -> Res.string.error_sale_owner
 "Only the owner can cancel or reopen a listing." -> Res.string.error_reopen_owner
 "Only the owner can delete this listing." -> Res.string.error_delete_owner
 "Unable to load listings." -> Res.string.error_load
 "Unable to search listings." -> Res.string.error_search
 else -> Res.string.error_save
}

fun com.example.swapcampus_zheng_li.ui.viewmodel.UiMessage.resource(): StringResource = when (this) {
    com.example.swapcampus_zheng_li.ui.viewmodel.UiMessage.POSTED -> Res.string.listing_posted
    com.example.swapcampus_zheng_li.ui.viewmodel.UiMessage.UPDATED -> Res.string.listing_updated
    com.example.swapcampus_zheng_li.ui.viewmodel.UiMessage.RESERVED -> Res.string.listing_reserved
    com.example.swapcampus_zheng_li.ui.viewmodel.UiMessage.RESERVATION_CANCELED -> Res.string.listing_reservation_canceled
    com.example.swapcampus_zheng_li.ui.viewmodel.UiMessage.SOLD -> Res.string.listing_sold
    com.example.swapcampus_zheng_li.ui.viewmodel.UiMessage.MADE_AVAILABLE -> Res.string.listing_made_available
    com.example.swapcampus_zheng_li.ui.viewmodel.UiMessage.DELETED -> Res.string.listing_deleted
}
