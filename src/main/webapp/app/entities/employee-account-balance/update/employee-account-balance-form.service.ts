import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEmployeeAccountBalance, NewEmployeeAccountBalance } from '../employee-account-balance.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEmployeeAccountBalance for edit and NewEmployeeAccountBalanceFormGroupInput for create.
 */
type EmployeeAccountBalanceFormGroupInput = IEmployeeAccountBalance | PartialWithRequiredKeyOf<NewEmployeeAccountBalance>;

type EmployeeAccountBalanceFormDefaults = Pick<NewEmployeeAccountBalance, 'id'>;

type EmployeeAccountBalanceFormGroupContent = {
  id: FormControl<IEmployeeAccountBalance['id'] | NewEmployeeAccountBalance['id']>;
  balance: FormControl<IEmployeeAccountBalance['balance']>;
  employee: FormControl<IEmployeeAccountBalance['employee']>;
  merchant: FormControl<IEmployeeAccountBalance['merchant']>;
  transactionType: FormControl<IEmployeeAccountBalance['transactionType']>;
};

export type EmployeeAccountBalanceFormGroup = FormGroup<EmployeeAccountBalanceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EmployeeAccountBalanceFormService {
  createEmployeeAccountBalanceFormGroup(
    employeeAccountBalance: EmployeeAccountBalanceFormGroupInput = { id: null }
  ): EmployeeAccountBalanceFormGroup {
    const employeeAccountBalanceRawValue = {
      ...this.getFormDefaults(),
      ...employeeAccountBalance,
    };
    return new FormGroup<EmployeeAccountBalanceFormGroupContent>({
      id: new FormControl(
        { value: employeeAccountBalanceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      balance: new FormControl(employeeAccountBalanceRawValue.balance, {
        validators: [Validators.required],
      }),
      employee: new FormControl(employeeAccountBalanceRawValue.employee),
      merchant: new FormControl(employeeAccountBalanceRawValue.merchant),
      transactionType: new FormControl(employeeAccountBalanceRawValue.transactionType),
    });
  }

  getEmployeeAccountBalance(form: EmployeeAccountBalanceFormGroup): IEmployeeAccountBalance | NewEmployeeAccountBalance {
    return form.getRawValue() as IEmployeeAccountBalance | NewEmployeeAccountBalance;
  }

  resetForm(form: EmployeeAccountBalanceFormGroup, employeeAccountBalance: EmployeeAccountBalanceFormGroupInput): void {
    const employeeAccountBalanceRawValue = { ...this.getFormDefaults(), ...employeeAccountBalance };
    form.reset(
      {
        ...employeeAccountBalanceRawValue,
        id: { value: employeeAccountBalanceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EmployeeAccountBalanceFormDefaults {
    return {
      id: null,
    };
  }
}
