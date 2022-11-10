import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEmployeeType, NewEmployeeType } from '../employee-type.model';

export type PartialUpdateEmployeeType = Partial<IEmployeeType> & Pick<IEmployeeType, 'id'>;

export type EntityResponseType = HttpResponse<IEmployeeType>;
export type EntityArrayResponseType = HttpResponse<IEmployeeType[]>;

@Injectable({ providedIn: 'root' })
export class EmployeeTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/employee-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(employeeType: NewEmployeeType): Observable<EntityResponseType> {
    return this.http.post<IEmployeeType>(this.resourceUrl, employeeType, { observe: 'response' });
  }

  update(employeeType: IEmployeeType): Observable<EntityResponseType> {
    return this.http.put<IEmployeeType>(`${this.resourceUrl}/${this.getEmployeeTypeIdentifier(employeeType)}`, employeeType, {
      observe: 'response',
    });
  }

  partialUpdate(employeeType: PartialUpdateEmployeeType): Observable<EntityResponseType> {
    return this.http.patch<IEmployeeType>(`${this.resourceUrl}/${this.getEmployeeTypeIdentifier(employeeType)}`, employeeType, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEmployeeType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEmployeeType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEmployeeTypeIdentifier(employeeType: Pick<IEmployeeType, 'id'>): number {
    return employeeType.id;
  }

  compareEmployeeType(o1: Pick<IEmployeeType, 'id'> | null, o2: Pick<IEmployeeType, 'id'> | null): boolean {
    return o1 && o2 ? this.getEmployeeTypeIdentifier(o1) === this.getEmployeeTypeIdentifier(o2) : o1 === o2;
  }

  addEmployeeTypeToCollectionIfMissing<Type extends Pick<IEmployeeType, 'id'>>(
    employeeTypeCollection: Type[],
    ...employeeTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const employeeTypes: Type[] = employeeTypesToCheck.filter(isPresent);
    if (employeeTypes.length > 0) {
      const employeeTypeCollectionIdentifiers = employeeTypeCollection.map(
        employeeTypeItem => this.getEmployeeTypeIdentifier(employeeTypeItem)!
      );
      const employeeTypesToAdd = employeeTypes.filter(employeeTypeItem => {
        const employeeTypeIdentifier = this.getEmployeeTypeIdentifier(employeeTypeItem);
        if (employeeTypeCollectionIdentifiers.includes(employeeTypeIdentifier)) {
          return false;
        }
        employeeTypeCollectionIdentifiers.push(employeeTypeIdentifier);
        return true;
      });
      return [...employeeTypesToAdd, ...employeeTypeCollection];
    }
    return employeeTypeCollection;
  }
}
