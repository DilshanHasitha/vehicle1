import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IImages, NewImages } from '../images.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IImages for edit and NewImagesFormGroupInput for create.
 */
type ImagesFormGroupInput = IImages | PartialWithRequiredKeyOf<NewImages>;

type ImagesFormDefaults = Pick<NewImages, 'id' | 'banners'>;

type ImagesFormGroupContent = {
  id: FormControl<IImages['id'] | NewImages['id']>;
  imglobContentType: FormControl<IImages['imglobContentType']>;
  imageURL: FormControl<IImages['imageURL']>;
  imageName: FormControl<IImages['imageName']>;
  lowResURL: FormControl<IImages['lowResURL']>;
  originalURL: FormControl<IImages['originalURL']>;
  imageBlob: FormControl<IImages['imageBlob']>;
  imageBlobContentType: FormControl<IImages['imageBlobContentType']>;
  employee: FormControl<IImages['employee']>;
  banners: FormControl<IImages['banners']>;
};

export type ImagesFormGroup = FormGroup<ImagesFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ImagesFormService {
  createImagesFormGroup(images: ImagesFormGroupInput = { id: null }): ImagesFormGroup {
    const imagesRawValue = {
      ...this.getFormDefaults(),
      ...images,
    };
    return new FormGroup<ImagesFormGroupContent>({
      id: new FormControl(
        { value: imagesRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      imglobContentType: new FormControl(imagesRawValue.imglobContentType),
      imageURL: new FormControl(imagesRawValue.imageURL),
      imageName: new FormControl(imagesRawValue.imageName),
      lowResURL: new FormControl(imagesRawValue.lowResURL),
      originalURL: new FormControl(imagesRawValue.originalURL),
      imageBlob: new FormControl(imagesRawValue.imageBlob),
      imageBlobContentType: new FormControl(imagesRawValue.imageBlobContentType),
      employee: new FormControl(imagesRawValue.employee),
      banners: new FormControl(imagesRawValue.banners ?? []),
    });
  }

  getImages(form: ImagesFormGroup): IImages | NewImages {
    return form.getRawValue() as IImages | NewImages;
  }

  resetForm(form: ImagesFormGroup, images: ImagesFormGroupInput): void {
    const imagesRawValue = { ...this.getFormDefaults(), ...images };
    form.reset(
      {
        ...imagesRawValue,
        id: { value: imagesRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ImagesFormDefaults {
    return {
      id: null,
      banners: [],
    };
  }
}
