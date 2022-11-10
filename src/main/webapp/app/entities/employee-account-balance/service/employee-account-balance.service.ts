import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEmployeeAccountBalance, NewEmployeeAccountBalance } from '../employee-account-balance.model';

export type PartialUpdateEmployeeAccountBalance = Partial<IEmployeeAccountBalance> & Pick<IEmployeeAccountBalance, 'id'>;

export type EntityResponseType = HttpResponse<IEmployeeAccountBalance>;
export type EntityArrayResponseType = HttpResponse<IEmployeeAccountBalance[]>;

@Injectable({ providedIn: 'root' })
export class EmployeeAccountBalanceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/employee-account-balances');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(employeeAccountBalance: NewEmployeeAccountBalance): Observable<EntityResponseType> {
    return this.http.post<IEmployeeAccountBalance>(this.resourceUrl, employeeAccountBalance, { observe: 'response' });
  }

  update(employeeAccountBalance: IEmployeeAccountBalance): Observable<EntityResponseType> {
    return this.http.put<IEmployeeAccountBalance>(
      `${this.resourceUrl}/${this.getEmployeeAccountBalanceIdentifier(employeeAccountBalance)}`,
      employeeAccountBalance,
      { observe: 'response' }
    );
  }

  partialUpdate(employeeAccountBalance: PartialUpdateEmployeeAccountBalance): Observable<EntityResponseType> {
    return this.http.patch<IEmployeeAccountBalance>(
      `${this.resourceUrl}/${this.getEmployeeAccountBalanceIdentifier(employeeAccountBalance)}`,
      employeeAccountBalance,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEmployeeAccountBalance>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEmployeeAccountBalance[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEmployeeAccountBalanceIdentifier(employeeAccountBalance: Pick<IEmployeeAccountBalance, 'id'>): number {
    return employeeAccountBalance.id;
  }

  compareEmployeeAccountBalance(o1: Pick<IEmployeeAccountBalance, 'id'> | null, o2: Pick<IEmployeeAccountBalance, 'id'> | null): boolean {
    return o1 && o2 ? this.getEmployeeAccountBalanceIdentifier(o1) === this.getEmployeeAccountBalanceIdentifier(o2) : o1 === o2;
  }

  addEmployeeAccountBalanceToCollectionIfMissing<Type extends Pick<IEmployeeAccountBalance, 'id'>>(
    employeeAccountBalanceCollection: Type[],
    ...employeeAccountBalancesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const employeeAccountBalances: Type[] = employeeAccountBalancesToCheck.filter(isPresent);
    if (employeeAccountBalances.length > 0) {
      const employeeAccountBalanceCollectionIdentifiers = employeeAccountBalanceCollection.map(
        employeeAccountBalanceItem => this.getEmployeeAccountBalanceIdentifier(employeeAccountBalanceItem)!
      );
      const employeeAccountBalancesToAdd = employeeAccountBalances.filter(employeeAccountBalanceItem => {
        const employeeAccountBalanceIdentifier = this.getEmployeeAccountBalanceIdentifier(employeeAccountBalanceItem);
        if (employeeAccountBalanceCollectionIdentifiers.includes(employeeAccountBalanceIdentifier)) {
          return false;
        }
        employeeAccountBalanceCollectionIdentifiers.push(employeeAccountBalanceIdentifier);
        return true;
      });
      return [...employeeAccountBalancesToAdd, ...employeeAccountBalanceCollection];
    }
    return employeeAccountBalanceCollection;
  }
}
