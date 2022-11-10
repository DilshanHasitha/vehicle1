import { IEmployee } from 'app/entities/employee/employee.model';
import { IMerchant } from 'app/entities/merchant/merchant.model';
import { ITransactionType } from 'app/entities/transaction-type/transaction-type.model';

export interface IEmployeeAccountBalance {
  id: number;
  balance?: number | null;
  employee?: Pick<IEmployee, 'id'> | null;
  merchant?: Pick<IMerchant, 'id'> | null;
  transactionType?: Pick<ITransactionType, 'id'> | null;
}

export type NewEmployeeAccountBalance = Omit<IEmployeeAccountBalance, 'id'> & { id: null };
