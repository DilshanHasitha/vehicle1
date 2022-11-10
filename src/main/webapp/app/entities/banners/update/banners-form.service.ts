import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IBanners, NewBanners } from '../banners.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBanners for edit and NewBannersFormGroupInput for create.
 */
type BannersFormGroupInput = IBanners | PartialWithRequiredKeyOf<NewBanners>;

type BannersFormDefaults = Pick<NewBanners, 'id' | 'images'>;

type BannersFormGroupContent = {
  id: FormControl<IBanners['id'] | NewBanners['id']>;
  code: FormControl<IBanners['code']>;
  heading: FormControl<IBanners['heading']>;
  description: FormControl<IBanners['description']>;
  link: FormControl<IBanners['link']>;
  images: FormControl<IBanners['images']>;
};

export type BannersFormGroup = FormGroup<BannersFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BannersFormService {
  createBannersFormGroup(banners: BannersFormGroupInput = { id: null }): BannersFormGroup {
    const bannersRawValue = {
      ...this.getFormDefaults(),
      ...banners,
    };
    return new FormGroup<BannersFormGroupContent>({
      id: new FormControl(
        { value: bannersRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      code: new FormControl(bannersRawValue.code),
      heading: new FormControl(bannersRawValue.heading),
      description: new FormControl(bannersRawValue.description),
      link: new FormControl(bannersRawValue.link),
      images: new FormControl(bannersRawValue.images ?? []),
    });
  }

  getBanners(form: BannersFormGroup): IBanners | NewBanners {
    return form.getRawValue() as IBanners | NewBanners;
  }

  resetForm(form: BannersFormGroup, banners: BannersFormGroupInput): void {
    const bannersRawValue = { ...this.getFormDefaults(), ...banners };
    form.reset(
      {
        ...bannersRawValue,
        id: { value: bannersRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): BannersFormDefaults {
    return {
      id: null,
      images: [],
    };
  }
}
