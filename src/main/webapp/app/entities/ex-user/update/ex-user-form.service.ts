import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IExUser, NewExUser } from '../ex-user.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IExUser for edit and NewExUserFormGroupInput for create.
 */
type ExUserFormGroupInput = IExUser | PartialWithRequiredKeyOf<NewExUser>;

type ExUserFormDefaults = Pick<NewExUser, 'id' | 'isActive' | 'merchants'>;

type ExUserFormGroupContent = {
  id: FormControl<IExUser['id'] | NewExUser['id']>;
  login: FormControl<IExUser['login']>;
  firstName: FormControl<IExUser['firstName']>;
  lastName: FormControl<IExUser['lastName']>;
  email: FormControl<IExUser['email']>;
  isActive: FormControl<IExUser['isActive']>;
  phone: FormControl<IExUser['phone']>;
  addressLine1: FormControl<IExUser['addressLine1']>;
  addressLine2: FormControl<IExUser['addressLine2']>;
  city: FormControl<IExUser['city']>;
  country: FormControl<IExUser['country']>;
  image: FormControl<IExUser['image']>;
  imageContentType: FormControl<IExUser['imageContentType']>;
  userLimit: FormControl<IExUser['userLimit']>;
  creditScore: FormControl<IExUser['creditScore']>;
  relatedUser: FormControl<IExUser['relatedUser']>;
  merchants: FormControl<IExUser['merchants']>;
};

export type ExUserFormGroup = FormGroup<ExUserFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ExUserFormService {
  createExUserFormGroup(exUser: ExUserFormGroupInput = { id: null }): ExUserFormGroup {
    const exUserRawValue = {
      ...this.getFormDefaults(),
      ...exUser,
    };
    return new FormGroup<ExUserFormGroupContent>({
      id: new FormControl(
        { value: exUserRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      login: new FormControl(exUserRawValue.login, {
        validators: [Validators.required],
      }),
      firstName: new FormControl(exUserRawValue.firstName, {
        validators: [Validators.required],
      }),
      lastName: new FormControl(exUserRawValue.lastName),
      email: new FormControl(exUserRawValue.email, {
        validators: [Validators.required, Validators.pattern('^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$')],
      }),
      isActive: new FormControl(exUserRawValue.isActive),
      phone: new FormControl(exUserRawValue.phone, {
        validators: [Validators.required],
      }),
      addressLine1: new FormControl(exUserRawValue.addressLine1, {
        validators: [Validators.required],
      }),
      addressLine2: new FormControl(exUserRawValue.addressLine2),
      city: new FormControl(exUserRawValue.city, {
        validators: [Validators.required],
      }),
      country: new FormControl(exUserRawValue.country, {
        validators: [Validators.required],
      }),
      image: new FormControl(exUserRawValue.image),
      imageContentType: new FormControl(exUserRawValue.imageContentType),
      userLimit: new FormControl(exUserRawValue.userLimit, {
        validators: [Validators.required],
      }),
      creditScore: new FormControl(exUserRawValue.creditScore),
      relatedUser: new FormControl(exUserRawValue.relatedUser),
      merchants: new FormControl(exUserRawValue.merchants ?? []),
    });
  }

  getExUser(form: ExUserFormGroup): IExUser | NewExUser {
    return form.getRawValue() as IExUser | NewExUser;
  }

  resetForm(form: ExUserFormGroup, exUser: ExUserFormGroupInput): void {
    const exUserRawValue = { ...this.getFormDefaults(), ...exUser };
    form.reset(
      {
        ...exUserRawValue,
        id: { value: exUserRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ExUserFormDefaults {
    return {
      id: null,
      isActive: false,
      merchants: [],
    };
  }
}
