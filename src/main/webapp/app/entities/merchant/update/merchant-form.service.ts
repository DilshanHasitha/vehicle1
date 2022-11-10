import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IMerchant, NewMerchant } from '../merchant.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMerchant for edit and NewMerchantFormGroupInput for create.
 */
type MerchantFormGroupInput = IMerchant | PartialWithRequiredKeyOf<NewMerchant>;

type MerchantFormDefaults = Pick<NewMerchant, 'id' | 'isActive' | 'isSandBox' | 'exUsers'>;

type MerchantFormGroupContent = {
  id: FormControl<IMerchant['id'] | NewMerchant['id']>;
  code: FormControl<IMerchant['code']>;
  merchantSecret: FormControl<IMerchant['merchantSecret']>;
  name: FormControl<IMerchant['name']>;
  creditLimit: FormControl<IMerchant['creditLimit']>;
  isActive: FormControl<IMerchant['isActive']>;
  phone: FormControl<IMerchant['phone']>;
  addressLine1: FormControl<IMerchant['addressLine1']>;
  addressLine2: FormControl<IMerchant['addressLine2']>;
  city: FormControl<IMerchant['city']>;
  country: FormControl<IMerchant['country']>;
  percentage: FormControl<IMerchant['percentage']>;
  creditScore: FormControl<IMerchant['creditScore']>;
  email: FormControl<IMerchant['email']>;
  rating: FormControl<IMerchant['rating']>;
  leadTime: FormControl<IMerchant['leadTime']>;
  isSandBox: FormControl<IMerchant['isSandBox']>;
  storeDescription: FormControl<IMerchant['storeDescription']>;
  storeSecondaryDescription: FormControl<IMerchant['storeSecondaryDescription']>;
  images: FormControl<IMerchant['images']>;
  exUsers: FormControl<IMerchant['exUsers']>;
};

export type MerchantFormGroup = FormGroup<MerchantFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MerchantFormService {
  createMerchantFormGroup(merchant: MerchantFormGroupInput = { id: null }): MerchantFormGroup {
    const merchantRawValue = {
      ...this.getFormDefaults(),
      ...merchant,
    };
    return new FormGroup<MerchantFormGroupContent>({
      id: new FormControl(
        { value: merchantRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      code: new FormControl(merchantRawValue.code, {
        validators: [Validators.required],
      }),
      merchantSecret: new FormControl(merchantRawValue.merchantSecret, {
        validators: [Validators.required],
      }),
      name: new FormControl(merchantRawValue.name, {
        validators: [Validators.required],
      }),
      creditLimit: new FormControl(merchantRawValue.creditLimit, {
        validators: [Validators.required],
      }),
      isActive: new FormControl(merchantRawValue.isActive),
      phone: new FormControl(merchantRawValue.phone, {
        validators: [Validators.required],
      }),
      addressLine1: new FormControl(merchantRawValue.addressLine1, {
        validators: [Validators.required],
      }),
      addressLine2: new FormControl(merchantRawValue.addressLine2),
      city: new FormControl(merchantRawValue.city, {
        validators: [Validators.required],
      }),
      country: new FormControl(merchantRawValue.country, {
        validators: [Validators.required],
      }),
      percentage: new FormControl(merchantRawValue.percentage, {
        validators: [Validators.required],
      }),
      creditScore: new FormControl(merchantRawValue.creditScore),
      email: new FormControl(merchantRawValue.email, {
        validators: [Validators.required, Validators.pattern('^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$')],
      }),
      rating: new FormControl(merchantRawValue.rating),
      leadTime: new FormControl(merchantRawValue.leadTime),
      isSandBox: new FormControl(merchantRawValue.isSandBox),
      storeDescription: new FormControl(merchantRawValue.storeDescription),
      storeSecondaryDescription: new FormControl(merchantRawValue.storeSecondaryDescription),
      images: new FormControl(merchantRawValue.images),
      exUsers: new FormControl(merchantRawValue.exUsers ?? []),
    });
  }

  getMerchant(form: MerchantFormGroup): IMerchant | NewMerchant {
    return form.getRawValue() as IMerchant | NewMerchant;
  }

  resetForm(form: MerchantFormGroup, merchant: MerchantFormGroupInput): void {
    const merchantRawValue = { ...this.getFormDefaults(), ...merchant };
    form.reset(
      {
        ...merchantRawValue,
        id: { value: merchantRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): MerchantFormDefaults {
    return {
      id: null,
      isActive: false,
      isSandBox: false,
      exUsers: [],
    };
  }
}
