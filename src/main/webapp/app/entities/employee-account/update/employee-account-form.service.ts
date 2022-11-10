import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEmployeeAccount, NewEmployeeAccount } from '../employee-account.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEmployeeAccount for edit and NewEmployeeAccountFormGroupInput for create.
 */
type EmployeeAccountFormGroupInput = IEmployeeAccount | PartialWithRequiredKeyOf<NewEmployeeAccount>;

type EmployeeAccountFormDefaults = Pick<NewEmployeeAccount, 'id'>;

type EmployeeAccountFormGroupContent = {
  id: FormControl<IEmployeeAccount['id'] | NewEmployeeAccount['id']>;
  transactionDate: FormControl<IEmployeeAccount['transactionDate']>;
  transactionDescription: FormControl<IEmployeeAccount['transactionDescription']>;
  transactionAmountDR: FormControl<IEmployeeAccount['transactionAmountDR']>;
  transactionAmountCR: FormControl<IEmployeeAccount['transactionAmountCR']>;
  transactionBalance: FormControl<IEmployeeAccount['transactionBalance']>;
  transactionType: FormControl<IEmployeeAccount['transactionType']>;
  merchant: FormControl<IEmployeeAccount['merchant']>;
  employee: FormControl<IEmployeeAccount['employee']>;
};

export type EmployeeAccountFormGroup = FormGroup<EmployeeAccountFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EmployeeAccountFormService {
  createEmployeeAccountFormGroup(employeeAccount: EmployeeAccountFormGroupInput = { id: null }): EmployeeAccountFormGroup {
    const employeeAccountRawValue = {
      ...this.getFormDefaults(),
      ...employeeAccount,
    };
    return new FormGroup<EmployeeAccountFormGroupContent>({
      id: new FormControl(
        { value: employeeAccountRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      transactionDate: new FormControl(employeeAccountRawValue.transactionDate, {
        validators: [Validators.required],
      }),
      transactionDescription: new FormControl(employeeAccountRawValue.transactionDescription, {
        validators: [Validators.required],
      }),
      transactionAmountDR: new FormControl(employeeAccountRawValue.transactionAmountDR, {
        validators: [Validators.required],
      }),
      transactionAmountCR: new FormControl(employeeAccountRawValue.transactionAmountCR, {
        validators: [Validators.required],
      }),
      transactionBalance: new FormControl(employeeAccountRawValue.transactionBalance, {
        validators: [Validators.required],
      }),
      transactionType: new FormControl(employeeAccountRawValue.transactionType),
      merchant: new FormControl(employeeAccountRawValue.merchant),
      employee: new FormControl(employeeAccountRawValue.employee),
    });
  }

  getEmployeeAccount(form: EmployeeAccountFormGroup): IEmployeeAccount | NewEmployeeAccount {
    return form.getRawValue() as IEmployeeAccount | NewEmployeeAccount;
  }

  resetForm(form: EmployeeAccountFormGroup, employeeAccount: EmployeeAccountFormGroupInput): void {
    const employeeAccountRawValue = { ...this.getFormDefaults(), ...employeeAccount };
    form.reset(
      {
        ...employeeAccountRawValue,
        id: { value: employeeAccountRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EmployeeAccountFormDefaults {
    return {
      id: null,
    };
  }
}
