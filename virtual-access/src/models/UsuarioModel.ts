/**
 * Description:</b> FIXME: Document this type <br>
 * Project:</b> virtual-access <br>
 *
 * author: macelai
 * date: 22 de jul de 2019
 * version $
 */

import {
  Column,
  CreatedAt,
  DataType,
  Model,
  Table,
  UpdatedAt,
  BeforeUpdate,
  BeforeCreate,
} from 'sequelize-typescript';
import PerfilType from '../enums/PerfilType';
import { genSaltSync, hashSync, compareSync } from 'bcryptjs';

@Table({
  tableName: 'RV_USUARIO',
})
export class UsuarioModel extends Model<UsuarioModel> {
  @Column({
    field: 'nome',
    type: DataType.STRING(50),
  })
  public nome!: string;

  @CreatedAt
  @Column({
    field: 'criacao',
    type: DataType.DATE,
    allowNull: false,
  })
  public criacao!: Date;

  @UpdatedAt
  @Column({
    field: 'alteracao',
    type: DataType.DATE,
  })
  public alteracao!: Date;

  @Column({
    field: 'ativo',
    allowNull: false,
    defaultValue: false,
  })
  public ativo!: boolean;

  @Column({
    field: 'ultima_troca_Senha',
    type: DataType.DATE,
  })
  public ultimaTrocaSenha!: Date;

  @Column({ field: 'email' })
  public email!: string;

  @Column({
    field: 'senha',
  })
  public senha!: string;

  @Column({
    field: 'alterar_senha',
    allowNull: false,
    defaultValue: false,
  })
  public alterarSenha!: boolean;

  @Column({
    field: 'bloqueado',
    allowNull: false,
    defaultValue: false,
  })
  public bloqueado!: boolean;

  @Column({
    field: 'tentativas_autenticacao',
    allowNull: false,
    defaultValue: 0,
  })
  public tentativasDeAutenticacao!: number;

  @Column({
    field: 'perfil_type',
    allowNull: false,
    defaultValue: PerfilType.Padrao,
    type: DataType.ENUM(PerfilType.Padrao, PerfilType.Adminstrador),
  })
  public perfilType!: string;

  @BeforeCreate
  static onCreate(instance: UsuarioModel) {
    const salt = genSaltSync();
    instance.senha = hashSync(instance.senha, salt);
  }

  @BeforeUpdate
  static onUpdate(instance: UsuarioModel) {
    if (instance.changed('senha')) {
      const salt = genSaltSync();
      instance.senha = hashSync(instance.senha, salt);
    }
  }

  public verificarSenha = (encodedSenha: string, senha: string): boolean => {
    return compareSync(senha, encodedSenha);
  }
}
