package shared 

enum class Categorie { ELECTRONICS, BOOKS, FURNITURE, CLOTHING, OTHER }
enum class EtatAnnonce { AVAILABLE, RESERVED, SOLD }
enum class RoleUtilisateur { STUDENT, ADMIN }
enum class TypeActionModeration { APPROVE, REJECT, FLAG }

data class CompteUtilisateur(
    val id: String,
    val email: String,
    val passwordHash: String,
    val name: String,
    val role: RoleUtilisateur = RoleUtilisateur.STUDENT
)

data class Annonce(
    val id: String,
    val title: String,
    val description: String,
    val price: Double,
    val category: Categorie,
    val imageUrl: String? = null,
    val ownerId: String,
    val status: EtatAnnonce = EtatAnnonce.AVAILABLE,
    val saleDate: String? = null
)
