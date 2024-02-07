package presentation.signup

import core.ResourcePath

enum class CurrentProcessesPosition(
    val heading: String = "",
    val subTitle: String = "",
) {
    FillBio(
        heading = ResourcePath.String.bioHeading,
        subTitle = ResourcePath.String.bioSubTitle,
    ),
    PaymentMethod(
        heading = ResourcePath.String.paymentHeading,
        subTitle = ResourcePath.String.paymentSubTitle,
    ),
    UploadPhoto(
        heading = ResourcePath.String.uploadPhotoHeading,
        subTitle = ResourcePath.String.uploadPhotoSubTitle,
    ),
    PhotoPreview(
        heading = ResourcePath.String.previewPhotoHeading,
        subTitle = ResourcePath.String.previewPhotoSubTitle,
    ),
    Location(
        heading = ResourcePath.String.locationHeading,
        subTitle = ResourcePath.String.locationSubTitle,
    ),
}

enum class UploadPhotoOption(
    val icon: String,
    val label: String,
) {
    Camera(
        icon = ResourcePath.Drawable.iconCamera,
        label = ResourcePath.String.takePhoto,
    ),
    Gallery(
        icon = ResourcePath.Drawable.iconGallery,
        label = ResourcePath.String.fromGallery,
    ),
}
