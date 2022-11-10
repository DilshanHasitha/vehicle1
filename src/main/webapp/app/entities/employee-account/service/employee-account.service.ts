import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEmployeeAccount, NewEmployeeAccount } from '../employee-account.model';

export type PartialUpdateEmployeeAccount = Partial<IEmployeeAccount> & Pick<IEmployeeAccount, 'id'>;

type RestOf<T extends IEmployeeAccount | NewEmployeeAccount> = Omit<T, 'transactionDate'> & {
  transactionDate?: string | null;
};

export type RestEmployeeAccount = RestOf<IEmployeeAccount>;

export type NewRestEmployeeAccount = RestOf<NewEmployeeAccount>;

export type PartialUpdateRestEmployeeAccount = RestOf<PartialUpdateEmployeeAccount>;

export type EntityResponseType = HttpResponse<IEmployeeAccount>;
export type EntityArrayResponseType = HttpResponse<IEmployeeAccount[]>;

@Injectable({ providedIn: 'root' })
export class EmployeeAccountService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/employee-accounts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(employeeAccount: NewEmployeeAccount): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employeeAccount);
    return this.http
      .post<RestEmployeeAccount>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(employeeAccount: IEmployeeAccount): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employeeAccount);
    return this.http
      .put<RestEmployeeAccount>(`${this.resourceUrl}/${this.getEmployeeAccountIdentifier(employeeAccount)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(employeeAccount: PartialUpdateEmployeeAccount): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employeeAccount);
    return this.http
      .patch<RestEmployeeAccount>(`${this.resourceUrl}/${this.getEmployeeAccountIdentifier(employeeAccount)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestEmployeeAccount>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestEmployeeAccount[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEmployeeAccountIdentifier(employeeAccount: Pick<IEmployeeAccount, 'id'>): number {
    return employeeAccount.id;
  }

  compareEmployeeAccount(o1: Pick<IEmployeeAccount, 'id'> | null, o2: Pick<IEmployeeAccount, 'id'> | null): boolean {
    return o1 && o2 ? this.getEmployeeAccountIdentifier(o1) === this.getEmployeeAccountIdentifier(o2) : o1 === o2;
  }

  addEmployeeAccountToCollectionIfMissing<Type extends Pick<IEmployeeAccount, 'id'>>(
    employeeAccountCollection: Type[],
    ...employeeAccountsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const employeeAccounts: Type[] = employeeAccountsToCheck.filter(isPresent);
    if (employeeAccounts.length > 0) {
      const employeeAccountCollectionIdentifiers = employeeAccountCollection.map(
        employeeAccountItem => this.getEmployeeAccountIdentifier(employeeAccountItem)!
      );
      const employeeAccountsToAdd = employeeAccounts.filter(employeeAccountItem => {
        const employeeAccountIdentifier = this.getEmployeeAccountIdentifier(employeeAccountItem);
        if (employeeAccountCollectionIdentifiers.includes(employeeAccountIdentifier)) {
          return false;
        }
        employeeAccountCollectionIdentifiers.push(employeeAccountIdentifier);
        return true;
      });
      return [...employeeAccountsToAdd, ...employeeAccountCollection];
    }
    return employeeAccountCollection;
  }

  protected convertDateFromClient<T extends IEmployeeAccount | NewEmployeeAccount | PartialUpdateEmployeeAccount>(
    employeeAccount: T
  ): RestOf<T> {
    return {
      ...employeeAccount,
      transactionDate: employeeAccount.transactionDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restEmployeeAccount: RestEmployeeAccount): IEmployeeAccount {
    return {
      ...restEmployeeAccount,
      transactionDate: restEmployeeAccount.transactionDate ? dayjs(restEmployeeAccount.transactionDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestEmployeeAccount>): HttpResponse<IEmployeeAccount> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestEmployeeAccount[]>): HttpResponse<IEmployeeAccount[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
