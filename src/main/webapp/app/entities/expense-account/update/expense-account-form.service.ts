import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IExpenseAccount, NewExpenseAccount } from '../expense-account.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IExpenseAccount for edit and NewExpenseAccountFormGroupInput for create.
 */
type ExpenseAccountFormGroupInput = IExpenseAccount | PartialWithRequiredKeyOf<NewExpenseAccount>;

type ExpenseAccountFormDefaults = Pick<NewExpenseAccount, 'id'>;

type ExpenseAccountFormGroupContent = {
  id: FormControl<IExpenseAccount['id'] | NewExpenseAccount['id']>;
  transactionDate: FormControl<IExpenseAccount['transactionDate']>;
  transactionDescription: FormControl<IExpenseAccount['transactionDescription']>;
  transactionAmountDR: FormControl<IExpenseAccount['transactionAmountDR']>;
  transactionAmountCR: FormControl<IExpenseAccount['transactionAmountCR']>;
  transactionBalance: FormControl<IExpenseAccount['transactionBalance']>;
  transactionType: FormControl<IExpenseAccount['transactionType']>;
  merchant: FormControl<IExpenseAccount['merchant']>;
  expense: FormControl<IExpenseAccount['expense']>;
};

export type ExpenseAccountFormGroup = FormGroup<ExpenseAccountFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ExpenseAccountFormService {
  createExpenseAccountFormGroup(expenseAccount: ExpenseAccountFormGroupInput = { id: null }): ExpenseAccountFormGroup {
    const expenseAccountRawValue = {
      ...this.getFormDefaults(),
      ...expenseAccount,
    };
    return new FormGroup<ExpenseAccountFormGroupContent>({
      id: new FormControl(
        { value: expenseAccountRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      transactionDate: new FormControl(expenseAccountRawValue.transactionDate, {
        validators: [Validators.required],
      }),
      transactionDescription: new FormControl(expenseAccountRawValue.transactionDescription, {
        validators: [Validators.required],
      }),
      transactionAmountDR: new FormControl(expenseAccountRawValue.transactionAmountDR, {
        validators: [Validators.required],
      }),
      transactionAmountCR: new FormControl(expenseAccountRawValue.transactionAmountCR, {
        validators: [Validators.required],
      }),
      transactionBalance: new FormControl(expenseAccountRawValue.transactionBalance, {
        validators: [Validators.required],
      }),
      transactionType: new FormControl(expenseAccountRawValue.transactionType),
      merchant: new FormControl(expenseAccountRawValue.merchant),
      expense: new FormControl(expenseAccountRawValue.expense),
    });
  }

  getExpenseAccount(form: ExpenseAccountFormGroup): IExpenseAccount | NewExpenseAccount {
    return form.getRawValue() as IExpenseAccount | NewExpenseAccount;
  }

  resetForm(form: ExpenseAccountFormGroup, expenseAccount: ExpenseAccountFormGroupInput): void {
    const expenseAccountRawValue = { ...this.getFormDefaults(), ...expenseAccount };
    form.reset(
      {
        ...expenseAccountRawValue,
        id: { value: expenseAccountRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ExpenseAccountFormDefaults {
    return {
      id: null,
    };
  }
}
